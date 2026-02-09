package com.guestkeeper.pro.utils

import android.content.Context
import android.os.Environment
import com.guestkeeper.pro.database.entity.Visit
import com.guestkeeper.pro.database.entity.Visitor
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object ReportGenerator {

    fun generateCSVReport(
        visits: List<Visit>,
        visitors: Map<Long, Visitor>,
        context: Context
    ): File {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "visit_report_${dateFormat.format(Date())}.csv"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(storageDir, fileName)

        CSVWriter(FileWriter(file)).use { writer ->
            // Write header
            writer.writeNext(
                arrayOf(
                    "Visit ID",
                    "Visitor Name",
                    "Email",
                    "Phone",
                    "Company",
                    "Type",
                    "Purpose",
                    "Host Employee",
                    "Arrival Time",
                    "Estimated Departure",
                    "Actual Departure",
                    "Status",
                    "Tag Number"
                )
            )

            // Write data
            visits.forEach { visit ->
                val visitor = visitors[visit.visitorId]
                writer.writeNext(
                    arrayOf(
                        visit.id.toString(),
                        visitor?.fullName ?: "N/A",
                        visitor?.email ?: "N/A",
                        visitor?.phone ?: "N/A",
                        visitor?.company ?: "N/A",
                        visitor?.userType?.toString() ?: "N/A",
                        visit.purpose ?: "N/A",
                        visit.hostEmployee ?: "N/A",
                        formatDate(visit.arrivalTime),
                        formatDate(visit.estimatedDeparture),
                        visit.actualDeparture?.let { formatDate(it) } ?: "N/A",
                        visit.status.toString(),
                        visit.tagId?.toString() ?: "N/A"
                    )
                )
            }
        }

        return file
    }

    fun generatePDFReport(
        visits: List<Visit>,
        visitors: Map<Long, Visitor>,
        companyName: String,
        context: Context
    ): File {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "visit_report_${dateFormat.format(Date())}.pdf"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(storageDir, fileName)

        PdfWriter(file).use { writer ->
            val pdf = PdfDocument(writer)
            val document = Document(pdf)

            // Header
            document.add(
                Paragraph("Visit Report - $companyName")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18f)
                    .setBold()
            )

            document.add(
                Paragraph("Generated: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10f)
                    .setFontColor(ColorConstants.GRAY)
            )

            document.add(Paragraph("\n"))

            // Summary
            document.add(
                Paragraph("Summary:")
                    .setFontSize(14f)
                    .setBold()
            )

            val totalVisits = visits.size
            val activeVisits = visits.count { it.status.toString() == "ACTIVE" }
            val completedVisits = visits.count { it.status.toString() == "COMPLETED" }

            document.add(
                Paragraph("Total Visits: $totalVisits")
                    .setFontSize(12f)
            )
            document.add(
                Paragraph("Active Visits: $activeVisits")
                    .setFontSize(12f)
            )
            document.add(
                Paragraph("Completed Visits: $completedVisits")
                    .setFontSize(12f)
            )

            document.add(Paragraph("\n"))

            // Table
            document.add(
                Paragraph("Visit Details:")
                    .setFontSize(14f)
                    .setBold()
            )

            val table = Table(floatArrayOf(1f, 2f, 2f, 2f, 2f, 2f))
            table.setWidthPercent(100f)

            // Table header
            addTableHeader(table, "ID")
            addTableHeader(table, "Visitor")
            addTableHeader(table, "Arrival")
            addTableHeader(table, "Departure")
            addTableHeader(table, "Status")
            addTableHeader(table, "Purpose")

            // Table data
            visits.forEach { visit ->
                val visitor = visitors[visit.visitorId]
                table.addCell(Cell().add(Paragraph(visit.id.toString())))
                table.addCell(Cell().add(Paragraph(visitor?.fullName ?: "N/A")))
                table.addCell(Cell().add(Paragraph(formatDate(visit.arrivalTime))))
                table.addCell(Cell().add(Paragraph(
                    visit.actualDeparture?.let { formatDate(it) } ?: formatDate(visit.estimatedDeparture)
                )))
                table.addCell(Cell().add(Paragraph(visit.status.toString())))
                table.addCell(Cell().add(Paragraph(visit.purpose ?: "N/A")))
            }

            document.add(table)

            // Footer
            document.add(
                Paragraph("\n\n--- End of Report ---")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10f)
                    .setFontColor(ColorConstants.GRAY)
            )

            document.close()
        }

        return file
    }

    private fun addTableHeader(table: Table, text: String) {
        table.addHeaderCell(
            Cell().add(Paragraph(text))
                .setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
        )
    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date)
    }

    fun getReportStats(visits: List<Visit>): Map<String, Any> {
        return mapOf(
            "total" to visits.size,
            "active" to visits.count { it.status.toString() == "ACTIVE" },
            "completed" to visits.count { it.status.toString() == "COMPLETED" },
            "overdue" to visits.count { it.status.toString() == "OVERDUE" },
            "today" to visits.count {
                SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(it.arrivalTime) ==
                        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            }
        )
    }
}

