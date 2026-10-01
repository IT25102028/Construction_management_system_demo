const token = localStorage.getItem("wbcms_token");
const userType = localStorage.getItem("wbcms_user_type");

if (!token || userType === "CLIENT") {
    window.location.replace("/staff-login.html");
}

const API = "/api/task-assignments";

let allAssignments = [];
let allTasks = [];
let allStaff = [];

let editingAssignmentId = null;


document.addEventListener("DOMContentLoaded", async () => {

    setupEvents();

    loadProfile();

    await loadTasks();

    await loadStaff();

    await loadAssignments();

});


function setupEvents() {

    document
        .getElementById("assignmentForm")
        .addEventListener(
            "submit",
            saveAssignment
        );


    document
        .getElementById("assignmentSearch")
        .addEventListener(
            "input",
            filterAssignments
        );


    document
        .getElementById("statusFilter")
        .addEventListener(
            "change",
            filterAssignments
        );


    document
        .getElementById("assignmentModal")
        .addEventListener(
            "click",
            function (event) {

                if (event.target === this) {
                    closeModal();
                }

            }
        );

}


function loadProfile() {

    const username =
        localStorage.getItem("wbcms_username")
        || "Staff User";

    const role =
        localStorage.getItem("wbcms_role")
        || "STAFF";


    document.getElementById("profileName")
        .textContent = username;


    document.getElementById("profileRole")
        .textContent = formatLabel(role);

}


function authHeaders() {

    return {
        "Content-Type": "application/json",

        "Authorization":
            `Bearer ${token}`
    };

}


async function loadTasks() {

    try {

        const response =
            await fetch(
                "/api/tasks?page=0&size=100&sort=title,asc",
                {
                    headers: {
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );


        if (!response.ok) {

            throw new Error(
                "Unable to load tasks."
            );

        }


        const data =
            await response.json();


        allTasks =
            data.content || [];


        const select =
            document.getElementById(
                "assignmentTask"
            );


        select.innerHTML =
            `<option value="">
                Select task
            </option>`;


        allTasks.forEach(task => {

            const option =
                document.createElement(
                    "option"
                );

            option.value =
                task.id;

            option.textContent =
                `${task.title} — ${task.projectName || "No Project"}`;

            select.appendChild(option);

        });


    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load tasks.",
            "error"
        );

    }

}


async function loadStaff() {

    try {

        const response =
            await fetch(
                "/api/users?page=0&size=100&sort=fullName,asc",
                {
                    headers: {
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );


        if (!response.ok) {

            throw new Error(
                "Unable to load staff members."
            );

        }


        const data =
            await response.json();


        const users =
            data.content || [];


        allStaff =
            users.filter(
                user =>
                    user.role !== "CLIENT"
            );


        const select =
            document.getElementById(
                "assignmentStaff"
            );


        select.innerHTML =
            `<option value="">
                Select staff member
            </option>`;


        allStaff.forEach(user => {

            const option =
                document.createElement(
                    "option"
                );


            option.value =
                user.id;


            option.textContent =
                `${user.fullName} — ${formatLabel(user.role)}`;


            select.appendChild(option);

        });


    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load staff members.",
            "error"
        );

    }

}


async function loadAssignments() {

    try {

        const response =
            await fetch(
                `${API}?page=0&size=100&sort=assignedDate,desc`,
                {
                    headers: {
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );


        if (!response.ok) {

            if (
                response.status === 401 ||
                response.status === 403
            ) {

                window.location.replace(
                    "/staff-login.html"
                );

                return;
            }


            throw new Error(
                "Unable to load assignments."
            );

        }


        const data =
            await response.json();


        allAssignments =
            data.content || [];


        updateStatistics();

        renderAssignments(
            allAssignments
        );


    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load assignments.",
            "error"
        );

    }

}


function renderAssignments(assignments) {

    const tbody =
        document.getElementById(
            "assignmentTableBody"
        );


    const empty =
        document.getElementById(
            "emptyTable"
        );


    tbody.innerHTML = "";


    if (!assignments.length) {

        empty.style.display = "flex";

        return;

    }


    empty.style.display = "none";


    assignments.forEach(
        assignment => {

            const row =
                document.createElement(
                    "tr"
                );


            row.innerHTML = `

                <td>
                    <div class="task-name">

                        <strong>
                            ${escapeHtml(
                assignment.taskTitle
            )}
                        </strong>

                        <span>
                            Task #${assignment.taskId}
                        </span>

                    </div>
                </td>


                <td>

                    <div class="staff-name">

                        <strong>
                            ${escapeHtml(
                assignment.staffName
            )}
                        </strong>

                        <span>
                            ${escapeHtml(
                assignment.staffUsername
            )}
                        </span>

                    </div>

                </td>


                <td>

                    <span class="role-badge">
                        ${escapeHtml(
                formatLabel(
                    assignment.staffRole
                )
            )}
                    </span>

                </td>


                <td>
                    ${formatDate(
                assignment.assignedDate
            )}
                </td>


                <td>

                    <span class="responsibility">
                        ${escapeHtml(
                assignment.responsibility
            )}
                    </span>

                </td>


                <td>
                    ${statusBadge(
                assignment.status
            )}
                </td>


                <td>

                    <div class="action-buttons">

                        <button
                            class="icon-btn view"
                            title="View"
                            onclick="viewAssignment(${assignment.id})">

                            <i class="bi bi-eye"></i>

                        </button>


                        <button
                            class="icon-btn edit"
                            title="Edit"
                            onclick="editAssignment(${assignment.id})">

                            <i class="bi bi-pencil"></i>

                        </button>


                        <button
                            class="icon-btn delete"
                            title="Delete"
                            onclick="deleteAssignment(${assignment.id})">

                            <i class="bi bi-trash"></i>

                        </button>

                    </div>

                </td>

            `;


            tbody.appendChild(row);

        }
    );

}


function statusBadge(status) {

    const labels = {

        ACTIVE: "Active",

        COMPLETED: "Completed",

        CANCELLED: "Cancelled"

    };


    return `
        <span class="status ${status.toLowerCase()}">
            ${labels[status] || status}
        </span>
    `;

}


function updateStatistics() {

    document.getElementById(
        "totalAssignments"
    ).textContent =
        allAssignments.length;


    document.getElementById(
        "activeAssignments"
    ).textContent =
        allAssignments.filter(
            item =>
                item.status === "ACTIVE"
        ).length;


    document.getElementById(
        "completedAssignments"
    ).textContent =
        allAssignments.filter(
            item =>
                item.status === "COMPLETED"
        ).length;


    document.getElementById(
        "cancelledAssignments"
    ).textContent =
        allAssignments.filter(
            item =>
                item.status === "CANCELLED"
        ).length;

}


function filterAssignments() {

    const search =
        document.getElementById(
            "assignmentSearch"
        ).value
            .toLowerCase()
            .trim();


    const status =
        document.getElementById(
            "statusFilter"
        ).value;


    const filtered =
        allAssignments.filter(
            assignment => {

                const searchableText =
                    [
                        assignment.taskTitle,
                        assignment.staffName,
                        assignment.staffUsername,
                        assignment.responsibility,
                        assignment.staffRole
                    ]
                        .filter(Boolean)
                        .join(" ")
                        .toLowerCase();


                const matchesSearch =
                    !search ||
                    searchableText.includes(
                        search
                    );


                const matchesStatus =
                    !status ||
                    assignment.status === status;


                return (
                    matchesSearch &&
                    matchesStatus
                );

            }
        );


    renderAssignments(filtered);

}


function openCreateModal() {

    editingAssignmentId = null;


    document.getElementById(
        "modalTitle"
    ).textContent =
        "Assign Staff";


    document.getElementById(
        "saveAssignmentBtn"
    ).innerHTML =
        `<i class="bi bi-check-lg"></i>
         Save Assignment`;


    document
        .getElementById("assignmentForm")
        .reset();


    document.getElementById(
        "assignmentStatus"
    ).value =
        "ACTIVE";


    document.getElementById(
        "assignedDate"
    ).value =
        new Date()
            .toISOString()
            .split("T")[0];


    document
        .getElementById("assignmentModal")
        .classList.add("show");

}


function editAssignment(id) {

    const assignment =
        allAssignments.find(
            item =>
                item.id === id
        );


    if (!assignment) return;


    editingAssignmentId =
        id;


    document.getElementById(
        "modalTitle"
    ).textContent =
        "Edit Assignment";


    document.getElementById(
        "saveAssignmentBtn"
    ).innerHTML =
        `<i class="bi bi-save"></i>
         Update Assignment`;


    document.getElementById(
        "assignmentId"
    ).value =
        assignment.id;


    document.getElementById(
        "assignmentTask"
    ).value =
        assignment.taskId;


    document.getElementById(
        "assignmentStaff"
    ).value =
        assignment.staffId;


    document.getElementById(
        "assignedDate"
    ).value =
        assignment.assignedDate;


    document.getElementById(
        "responsibility"
    ).value =
        assignment.responsibility || "";


    document.getElementById(
        "assignmentStatus"
    ).value =
        assignment.status;


    document
        .getElementById("assignmentModal")
        .classList.add("show");

}


async function saveAssignment(event) {

    event.preventDefault();

    clearErrors();


    const taskId =
        document.getElementById(
            "assignmentTask"
        ).value;


    const staffId =
        document.getElementById(
            "assignmentStaff"
        ).value;


    const assignedDate =
        document.getElementById(
            "assignedDate"
        ).value;


    const responsibility =
        document.getElementById(
            "responsibility"
        ).value.trim();


    const status =
        document.getElementById(
            "assignmentStatus"
        ).value;


    let valid = true;


    if (!taskId) {

        showError(
            "taskError",
            "Please select a task."
        );

        valid = false;

    }


    if (!staffId) {

        showError(
            "staffError",
            "Please select a staff member."
        );

        valid = false;

    }


    if (!assignedDate) {

        showError(
            "dateError",
            "Assigned date is required."
        );

        valid = false;

    }


    if (!responsibility) {

        showError(
            "responsibilityError",
            "Responsibility is required."
        );

        valid = false;

    }


    if (!valid) return;


    const payload = {

        taskId:
            Number(taskId),

        staffId:
            Number(staffId),

        assignedDate,

        responsibility,

        status

    };


    try {

        const url =
            editingAssignmentId
                ? `${API}/${editingAssignmentId}`
                : API;


        const method =
            editingAssignmentId
                ? "PUT"
                : "POST";


        const response =
            await fetch(
                url,
                {
                    method,

                    headers:
                        authHeaders(),

                    body:
                        JSON.stringify(
                            payload
                        )
                }
            );


        if (!response.ok) {

            const errorText =
                await response.text();


            throw new Error(
                errorText ||
                "Unable to save assignment."
            );

        }


        closeModal();

        await loadAssignments();


        showToast(
            editingAssignmentId
                ? "Assignment updated successfully."
                : "Staff assigned successfully."
        );


    } catch (error) {

        console.error(error);

        showToast(
            error.message ||
            "Unable to save assignment.",
            "error"
        );

    }

}


function viewAssignment(id) {

    const assignment =
        allAssignments.find(
            item =>
                item.id === id
        );


    if (!assignment) return;


    alert(
        `Assignment Details\n\n` +

        `Task: ${assignment.taskTitle}\n` +

        `Staff: ${assignment.staffName}\n` +

        `Role: ${formatLabel(
            assignment.staffRole
        )}\n` +

        `Assigned Date: ${formatDate(
            assignment.assignedDate
        )}\n` +

        `Status: ${formatLabel(
            assignment.status
        )}\n\n` +

        `Responsibility:\n` +

        `${assignment.responsibility}`
    );

}


async function deleteAssignment(id) {

    const assignment =
        allAssignments.find(
            item =>
                item.id === id
        );


    if (!assignment) return;


    const confirmed =
        confirm(
            `Remove ${assignment.staffName} from "${assignment.taskTitle}"?`
        );


    if (!confirmed) return;


    try {

        const response =
            await fetch(
                `${API}/${id}`,
                {
                    method: "DELETE",

                    headers: {
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );


        if (!response.ok) {

            throw new Error(
                "Unable to delete assignment."
            );

        }


        await loadAssignments();


        showToast(
            "Assignment deleted successfully."
        );


    } catch (error) {

        console.error(error);

        showToast(
            error.message ||
            "Unable to delete assignment.",
            "error"
        );

    }

}


function closeModal() {

    document
        .getElementById(
            "assignmentModal"
        )
        .classList.remove("show");

}


function clearErrors() {

    document
        .querySelectorAll(".error")
        .forEach(
            element => {
                element.textContent = "";
            }
        );

}


function showError(
    id,
    message
) {

    document.getElementById(id)
        .textContent =
        message;

}


function formatDate(date) {

    if (!date) return "-";


    return new Date(
        date + "T00:00:00"
    ).toLocaleDateString(
        "en-GB",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );

}


function formatLabel(value) {

    if (!value) return "-";


    return value
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            char =>
                char.toUpperCase()
        );

}


function escapeHtml(value) {

    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll(
            '"',
            "&quot;"
        )
        .replaceAll(
            "'",
            "&#039;"
        );

}


function showToast(
    message,
    type = "success"
) {

    const toast =
        document.getElementById(
            "toast"
        );


    const messageElement =
        document.getElementById(
            "toastMessage"
        );


    messageElement.textContent =
        message;


    toast.classList.remove(
        "error",
        "show"
    );


    if (type === "error") {

        toast.classList.add(
            "error"
        );

    }


    requestAnimationFrame(
        () => {
            toast.classList.add(
                "show"
            );
        }
    );


    setTimeout(
        () => {
            toast.classList.remove(
                "show"
            );
        },
        3500
    );

}


function logout() {

    const keys = [

        "wbcms_token",

        "wbcms_user_id",

        "wbcms_username",

        "wbcms_role",

        "wbcms_user_type",

        "wbcms_user"

    ];


    keys.forEach(
        key =>
            localStorage.removeItem(
                key
            )
    );


    window.location.replace(
        "/staff-login.html"
    );

}