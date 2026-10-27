/**
 * Employee Management System - Client Side Interactive Logic
 */

document.addEventListener('DOMContentLoaded', () => {

    // 1. Auto-dismiss flash alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });

    // 2. Setup Delete Employee Confirmation Modal
    const deleteModal = document.getElementById('deleteConfirmModal');
    if (deleteModal) {
        deleteModal.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget;
            const employeeId = button.getAttribute('data-employee-id');
            const employeeName = button.getAttribute('data-employee-name');

            const namePlaceholder = deleteModal.querySelector('#deleteEmployeeName');
            const deleteForm = deleteModal.querySelector('#deleteEmployeeForm');

            if (namePlaceholder) {
                namePlaceholder.textContent = employeeName;
            }
            if (deleteForm) {
                deleteForm.setAttribute('action', `/employees/delete/${employeeId}`);
            }
        });
    }

    // 3. Live Asynchronous Email Uniqueness Validation
    const emailInput = document.getElementById('employeeEmailInput');
    const emailFeedback = document.getElementById('emailLiveFeedback');
    const employeeIdInput = document.getElementById('employeeIdHidden');

    if (emailInput && emailFeedback) {
        let debounceTimer = null;

        emailInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            const email = emailInput.value.trim();
            const excludeId = employeeIdInput ? employeeIdInput.value : '';

            // Basic format check before server ping
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!email || !emailRegex.test(email)) {
                emailFeedback.textContent = '';
                emailInput.classList.remove('is-invalid', 'is-valid');
                return;
            }

            debounceTimer = setTimeout(() => {
                let url = `/employees/api/check-email?email=${encodeURIComponent(email)}`;
                if (excludeId) {
                    url += `&excludeId=${encodeURIComponent(excludeId)}`;
                }

                fetch(url)
                    .then(response => response.json())
                    .then(data => {
                        if (data.taken) {
                            emailInput.classList.add('is-invalid');
                            emailInput.classList.remove('is-valid');
                            emailFeedback.textContent = 'This email address is already in use by another employee.';
                            emailFeedback.className = 'invalid-feedback d-block';
                        } else {
                            emailInput.classList.remove('is-invalid');
                            emailInput.classList.add('is-valid');
                            emailFeedback.textContent = 'Email address is available.';
                            emailFeedback.className = 'valid-feedback d-block';
                        }
                    })
                    .catch(err => {
                        console.debug('Email check skipped:', err);
                    });
            }, 400); // 400ms debounce
        });
    }

    // 4. Live Clock in Top Navbar
    const liveClock = document.getElementById('liveClock');
    if (liveClock) {
        const updateClock = () => {
            const now = new Date();
            liveClock.textContent = now.toLocaleDateString(undefined, {
                weekday: 'short',
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            }) + ' • ' + now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
        };
        updateClock();
        setInterval(updateClock, 1000);
    }

    // 5. Sidebar Toggle for Small Screens
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('appSidebar');
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', () => {
            sidebar.classList.toggle('show');
        });
    }

    // 6. Bootstrap Form Submission Validation Enhancement
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

});
