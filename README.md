# GuestKeeper Pro - Visitor Management System

## Smart, Standalone Visitor Registration for Any Business

GuestKeeper Pro is a complete, self-contained visitor management system that runs entirely on your Android phone or tablet. Perfect for reception areas, where guests visit your companies, this app provides professional visitor registration without requiring internet connectivity or complex setup.

### Why GuestKeeper Pro?
- **100% Standalone**: No server costs, no monthly fees, works completely offline
- **Professional Registration**: Capture visitor details, photos, and assign tracking tags
- **Company Personalization**: Add your logo and company name for branded experience
- **Visit History**: Track all visits and export records via email
- **Simple Distribution**: Available on Google Play Store for one-time purchase

<img width="2304" height="1728" alt="image" src="https://github.com/user-attachments/assets/555e8917-39c2-42f6-a9ec-9fd3680b1dce" />

### Key Features
- Visitor/Supplier registration with photos
- Digital visitor logs and real-time presence tracking
- Tag-based visit management
- Exportable visit history reports
- Password-protected access
- Company branding customization

### AI-Generated Development
This entire project is developed using AI code generation. All features, imports, and files are created through AI prompts without manual coding.

**Target Platform**: Android (Kotlin/Java)
**Database**: SQLite (local storage)
**Minimum SDK**: API 24 (Android 7.0)

# Detailed requirements

## Core registration module

REQ-001: User Type Selection
- Radio buttons for "Guest" or "Supplier" registration
- Persistent user type categorization

REQ-002: Visitor Information Capture
- Required: Full name, email, phone number
- Required: Arrival time (auto-captured with manual override)
- Required: Estimated departure time (time picker)
- Required: Who to visit (choose host from list)
- Optional: Company name, visit purpose, host employee
- Photo capture button (opens camera)
- Auto-save on registration completion

REQ-003: Login Credentials Generation
- Auto-generate username (email-based)
- Auto-generate 8-character password (display once, store hashed)
- Password reset capability via email verification

## Tag Management System

REQ-004: Physical Tag Association
- Input field for tag number (manual entry)
- Tag number validation (unique per active visit)
- Tag status tracking: Active/Returned/Lost
- Tag number history per visitor

REQ-005: Active Visit Dashboard
- Real-time list of current visitors in building
- Sort by: Arrival time, Host, Name, Company
- Filter by: Guest/Supplier type
- Quick view of estimated departure times
- Color coding for overdue departures

## Visitor Management Features

REQ-006: Visitor Profile Management
- Edit all visitor details (except username)
- Update photo (retake or gallery)
- View visit history per person
- Merge duplicate profiles

REQ-007: Check-out System
- Manual check-out with actual departure time
- Auto-checkout at estimated departure time (with notification)
- Option to extend visit duration
- Generate visit summary on checkout

## Reporting & Export Module

REQ-008: Visit History Database
- Store all visits with timestamps
- Search by: Date range, Name, Email, Company
- Filter by: Visitor type, Tag number

REQ-009-01: Email Export Functionality
- Select multiple visits for export
- Generate CSV/PDF report
- Email to multiple recipients
- Include/exclude columns customization
- Automatic report naming convention

REQ-009-02: Status Synchronization
- Push updates to waiting screen when host reads email
- Auto-refresh status every 30 seconds
- WebSocket or polling implementation for real-time updates

## Company Personalization

REQ-010: Branding Configuration
- Company name setting (used in reports/UI)
- Logo upload (camera or gallery)
- Logo display on main screen and reports
- Custom welcome message
- Company contact info in footer

REQ-011: Local Configuration Storage
- All settings stored locally on device
- Export/import settings capability
- Reset to default functionality

## User Management & Security

REQ-012: Multi-User Support
- Admin vs Receptionist roles
- Admin: Full access, user management
- Receptionist: Registration only, no settings access
- User session timeout (configurable)

REQ-013-01: Data Security
- Local database encryption
- Password hashing (SHA-256)
- Auto-backup to local storage
- Data purge policy (configurable retention)

REQ-013-02: Data Protection
- GDPR-compliant email tracking
- Opt-out mechanism for hosts
- Visitor photo sharing consent toggle
- Data retention settings for tracking logs

## Camera & Media Features

REQ-014: Photo Capture
- Integrated camera interface
- Photo crop/rotate functionality
- Photo quality settings
- Storage optimization (size/compression)
- Gallery import option

REQ-015: Photo Management
- Visitor photo gallery per profile
- Default photo assignment
- Photo export with reports
- Privacy mode (optional photo disable)

## Offline Functionality

REQ-016: Complete Offline Operation
- Zero internet dependency
- Local SQLite database
- Periodic auto-save
- Conflict resolution for concurrent edits

REQ-017: Data Backup & Recovery
- Manual backup trigger
- Automatic daily backup
- Backup to device storage
- Restore from latest backup

## Google Play Store Requirements / Challenge

REQ-018: Distribution Readiness
- Google Play compliant APK
- Appropriate content rating
- Privacy policy integration
- Support email contact
- App description and screenshots

REQ-019: Purchase & Licensing
- One-time purchase flow
- License validation (offline-capable)
- Free trial period (7 days)
- Purchase restoration capability

## UI/UX Requirements

REQ-020: Responsive Design
- Tablet-optimized layouts
- Portrait and landscape support
- Accessibility features (TalkBack support)
- High-contrast mode

REQ-021: Navigation Flow
- Intuitive bottom navigation
- Quick actions on home screen
- Search functionality on all lists
- Confirmation dialogs for deletions

## Notification System

REQ-022: Local Notifications
- Departure reminders (15-minute advance)
- Overdue visitor alerts
- Daily summary notifications
- Backup completion alerts

## Performance Requirements

REQ-023: Performance Standards
- Launch time < 3 seconds
- Photo capture < 2 seconds
- List loading < 1 second
- Database operations < 500ms
- Support for 10,000+ visitor records

## Testing & Quality

REQ-024: Validation Requirements
- Email format validation
- Phone number format checking
- Required field validation
- Duplicate email prevention
- Date/time logic validation

REQ-025: Error Handling
- Graceful error messages
- Data corruption recovery
- Out-of-storage handling
- Camera permission handling

# AI Development Instructions:

This project must be developed using AI code generation exclusively. For each requirement:
1. Generate complete Kotlin/Java files
2. Generate all necessary XML layouts
3. Generate all imports and dependencies
4. Generate database schemas and migrations
5. Generate permission handling
6. Generate Google Play Store listing assets
7. Generate privacy policy document
8. Generate configuration files (build.gradle, etc.)

NO MANUAL CODE EDITING ALLOWED. All modifications must be made through AI prompts.

This specification provides a complete foundation for AI to generate a fully-functional visitor management app. Each requirement is atomic and testable, allowing for iterative AI-driven development.
