package com.guestkeeper.pro.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.guestkeeper.pro.database.AppDatabase
import com.guestkeeper.pro.databinding.ActivityVisitorRegistrationBinding
import com.guestkeeper.pro.model.UserType
import com.guestkeeper.pro.utils.CameraUtils
import com.guestkeeper.pro.utils.PreferenceManager
import com.guestkeeper.pro.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class VisitorRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisitorRegistrationBinding
    private lateinit var database: AppDatabase
    private lateinit var preferenceManager: PreferenceManager

    private var selectedUserType = UserType.GUEST
    private var arrivalTime = Calendar.getInstance()
    private var estimatedDeparture = Calendar.getInstance().apply { add(Calendar.HOUR, 1) }
    private var photoPath: String? = null
    private var selectedTagId: Long? = null

    companion object {
        const val REQUEST_CAMERA = 1001
        const val REQUEST_TAG_SELECTION = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitorRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        preferenceManager = PreferenceManager(this)

        setupUI()
        loadEmployeeList()
    }

    private fun setupUI() {
        // User type radio group
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedUserType = when (checkedId) {
                R.id.radioGuest -> UserType.GUEST
                R.id.radioSupplier -> UserType.SUPPLIER
                else -> UserType.GUEST
            }
        }

        // Set arrival time (now)
        updateArrivalTimeDisplay()

        // Arrival time picker
        binding.btnEditArrivalTime.setOnClickListener {
            showDateTimePicker(true)
        }

        // Estimated departure picker
        binding.etEstimatedDeparture.setOnClickListener {
            showDateTimePicker(false)
        }

        // Photo capture
        binding.btnCapturePhoto.setOnClickListener {
            openCamera()
        }

        // Tag selection
        binding.btnSelectTag.setOnClickListener {
            selectTag()
        }

        // Register button
        binding.btnRegister.setOnClickListener {
            registerVisitor()
        }

        // Cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun updateArrivalTimeDisplay() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        binding.tvArrivalTime.text = timeFormat.format(arrivalTime.time)
        binding.tvArrivalDate.text = dateFormat.format(arrivalTime.time)
    }

    private fun showDateTimePicker(isArrival: Boolean) {
        val calendar = if (isArrival) arrivalTime else estimatedDeparture
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, year, month, day ->
                val timePicker = TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        calendar.set(year, month, day, hour, minute)
                        
                        if (isArrival) {
                            updateArrivalTimeDisplay()
                        } else {
                            val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            binding.etEstimatedDeparture.setText(dateTimeFormat.format(calendar.time))
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePicker.show()
            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun openCamera() {
        lifecycleScope.launch {
            val photoFile = CameraUtils.capturePhoto(this@VisitorRegistrationActivity, "visitor")
            if (photoFile != null) {
                val intent = Intent(this@VisitorRegistrationActivity, CameraActivity::class.java)
                intent.putExtra("photo_file_path", photoFile.absolutePath)
                startActivityForResult(intent, REQUEST_CAMERA)
            }
        }
    }

    private fun selectTag() {
        val intent = Intent(this, TagSelectionActivity::class.java)
        startActivityForResult(intent, REQUEST_TAG_SELECTION)
    }

    private fun loadEmployeeList() {
        lifecycleScope.launch {
            val employees = withContext(Dispatchers.IO) {
                // Load employees from database or settings
                database.userDao().getAllReceptionists()
            }

            val employeeNames = employees.map { it.fullName ?: it.email }
            val adapter = ArrayAdapter(
                this@VisitorRegistrationActivity,
                android.R.layout.simple_dropdown_item_1line,
                employeeNames
            )
            binding.etHostEmployee.setAdapter(adapter)
        }
    }

    private fun registerVisitor() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val company = binding.etCompany.text.toString().trim()
        val purpose = binding.etPurpose.text.toString().trim()
        val hostEmployee = binding.etHostEmployee.text.toString().trim()

        if (!validateInput(fullName, email, phone)) {
            return
        }

        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnRegister.isEnabled = false

            try {
                // Generate credentials
                val username = SecurityUtils.generateUsername(email)
                val tempPassword = SecurityUtils.generateRandomPassword()
                val passwordHash = SecurityUtils.hashPassword(tempPassword)

                val visitorId = withContext(Dispatchers.IO) {
                    // Check for duplicate
                    val existingVisitor = database.visitorDao().findDuplicate(email, phone)
                    if (existingVisitor != null) {
                        // Update existing visitor
                        database.visitorDao().update(existingVisitor.copy(
                            fullName = fullName,
                            company = company,
                            updatedAt = Date()
                        ))
                        existingVisitor.id
                    } else {
                        // Create new visitor
                        val visitor = com.guestkeeper.pro.database.entity.Visitor(
                            fullName = fullName,
                            email = email,
                            phone = phone,
                            company = company,
                            photoPath = photoPath,
                            userType = selectedUserType,
                            username = username,
                            passwordHash = passwordHash,
                            notes = purpose
                        )
                        database.visitorDao().insert(visitor).toInt()
                    }
                }

                // Create visit record
                val userId = preferenceManager.getUserId()
                val visit = com.guestkeeper.pro.database.entity.Visit(
                    visitorId = visitorId,
                    tagId = selectedTagId,
                    purpose = purpose,
                    hostEmployee = hostEmployee,
                    arrivalTime = arrivalTime.time,
                    estimatedDeparture = estimatedDeparture.time,
                    createdBy = userId
                )

                withContext(Dispatchers.IO) {
                    database.visitDao().insert(visit)
                }

                // Update tag status if selected
                selectedTagId?.let { tagId ->
                    withContext(Dispatchers.IO) {
                        database.tagDao().updateTagStatus(tagId, "IN_USE", visit.id)
                    }
                }

                // Show success message with credentials
                showSuccessDialog(fullName, username, tempPassword)

            } catch (e: Exception) {
                Toast.makeText(
                    this@VisitorRegistrationActivity,
                    "Registration failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnRegister.isEnabled = true
            }
        }
    }

    private fun validateInput(fullName: String, email: String, phone: String): Boolean {
        var isValid = true

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name is required"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            isValid = false
        } else if (!SecurityUtils.isValidEmail(email)) {
            binding.etEmail.error = "Invalid email format"
            isValid = false
        }

        if (phone.isEmpty()) {
            binding.etPhone.error = "Phone number is required"
            isValid = false
        } else if (!SecurityUtils.isValidPhone(phone)) {
            binding.etPhone.error = "Invalid phone number"
            isValid = false
        }

        return isValid
    }

    private fun showSuccessDialog(fullName: String, username: String, password: String) {
        // TODO: Show success dialog with credentials
        Toast.makeText(
            this,
            "Visitor registered successfully!\nUsername: $username\nPassword: $password",
            Toast.LENGTH_LONG
        ).show()
        
        // Clear form
        clearForm()
    }

    private fun clearForm() {
        binding.etFullName.text.clear()
        binding.etEmail.text.clear()
        binding.etPhone.text.clear()
        binding.etCompany.text.clear()
        binding.etPurpose.text.clear()
        binding.etHostEmployee.text.clear()
        binding.etEstimatedDeparture.text.clear()
        photoPath = null
        selectedTagId = null
        binding.ivVisitorPhoto.setImageResource(R.drawable.ic_person)
        binding.tvTagNumber.text = "Not selected"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (resultCode == RESULT_OK && data != null) {
                    photoPath = data.getStringExtra("photo_path")
                    // Load and display photo
                    // TODO: Implement photo loading
                }
            }
            REQUEST_TAG_SELECTION -> {
                if (resultCode == RESULT_OK && data != null) {
                    selectedTagId = data.getLongExtra("tag_id", -1)
                    val tagNumber = data.getStringExtra("tag_number")
                    binding.tvTagNumber.text = tagNumber ?: "Not selected"
                }
            }
        }
    }
}

