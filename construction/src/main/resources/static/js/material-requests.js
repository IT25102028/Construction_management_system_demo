const API_URL = "/api/material-requests";

let requests = [];


// ===============================
// AUTHENTICATION
// ===============================

const token = localStorage.getItem("wbcms_token");
const userType = localStorage.getItem("wbcms_user_type");

if (!token || userType !== "STAFF") {
    window.location.replace("/staff-login.html");
}


// ===============================
// PAGE LOAD
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    loadRequests();

    document
        .getElementById("requestForm")
        .addEventListener("submit", saveRequest);

});


// ===============================
// API HEADERS
// ===============================

function getHeaders() {

    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };

}


// ===============================
// LOAD REQUESTS
// ===============================

async function loadRequests() {

    try {

        const search =
            document.getElementById("searchInput").value.trim();

        const status =
            document.getElementById("statusFilter").value;


        let url =
            `${API_URL}?page=0&size=200`;


        if (search) {
            url += `&search=${encodeURIComponent(search)}`;
        }

        if (status) {
            url += `&status=${encodeURIComponent(status)}`;
        }


        const response = await fetch(url, {
            headers: getHeaders()
        });


        if (!response.ok) {
            throw new Error(
                await getApiError(response)
            );
        }


        const data = await response.json();


        requests = data.content || [];


        renderRequests();

        updateStatistics();


    } catch (error) {

        console.error(error);

        showToast(
            error.message || "Failed to load material requests.",
            "error"
        );

    }

}


// ===============================
// RENDER TABLE
// ===============================

function renderRequests() {

    const tbody =
        document.getElementById("requestTableBody");

    const emptyMessage =
        document.getElementById("emptyMessage");


    tbody.innerHTML = "";


    if (requests.length === 0) {

        emptyMessage.style.display = "block";

        return;

    }


    emptyMessage.style.display = "none";


    requests.forEach(request => {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                <strong>${escapeHtml(request.requestCode)}</strong>
            </td>

            <td>
                ${escapeHtml(request.materialName)}
            </td>

            <td>
                ${request.quantity}
                ${escapeHtml(request.unit)}
            </td>

            <td>
                ${escapeHtml(request.projectName)}
            </td>

            <td>
                ${escapeHtml(request.requestedBy)}
            </td>

            <td>
                ${request.requiredDate || "-"}
            </td>

            <td>
                <span class="status ${getStatusClass(request.status)}">
                    ${request.status}
                </span>
            </td>

            <td class="actions">

                <button
                    class="view-btn"
                    onclick="viewRequest(${request.id})">
                    View
                </button>

                <button
                    class="edit-btn"
                    onclick="editRequest(${request.id})">
                    Edit
                </button>

                <button
                    class="delete-btn"
                    onclick="deleteRequest(${request.id})">
                    Delete
                </button>

            </td>

        `;


        tbody.appendChild(row);

    });

}


// ===============================
// STATISTICS
// ===============================

function updateStatistics() {

    const total =
        requests.length;

    const pending =
        requests.filter(r => r.status === "PENDING").length;

    const approved =
        requests.filter(r => r.status === "APPROVED").length;

    const fulfilled =
        requests.filter(r => r.status === "FULFILLED").length;


    document.getElementById("totalRequests")
        .textContent = total;

    document.getElementById("pendingRequests")
        .textContent = pending;

    document.getElementById("approvedRequests")
        .textContent = approved;

    document.getElementById("fulfilledRequests")
        .textContent = fulfilled;

}


// ===============================
// CREATE MODAL
// ===============================

function openCreateModal() {

    document.getElementById("modalTitle")
        .textContent = "New Material Request";


    document.getElementById("requestForm")
        .reset();


    document.getElementById("requestId")
        .value = "";


    document.getElementById("status")
        .value = "PENDING";


    document.getElementById("requestModal")
        .classList.add("show");

}


// ===============================
// CLOSE MODAL
// ===============================

function closeModal() {

    document.getElementById("requestModal")
        .classList.remove("show");

}


// ===============================
// SAVE REQUEST
// ===============================

async function saveRequest(event) {

    event.preventDefault();


    const id =
        document.getElementById("requestId").value;


    const requestData = {

        requestCode:
            document.getElementById("requestCode").value.trim(),

        materialName:
            document.getElementById("materialName").value.trim(),

        quantity:
            Number(document.getElementById("quantity").value),

        unit:
            document.getElementById("unit").value.trim(),

        projectName:
            document.getElementById("projectName").value.trim(),

        requestedBy:
            document.getElementById("requestedBy").value.trim(),

        requiredDate:
        document.getElementById("requiredDate").value,

        reason:
            document.getElementById("reason").value.trim(),

        status:
        document.getElementById("status").value

    };


    try {

        const response = await fetch(

            id
                ? `${API_URL}/${id}`
                : API_URL,

            {

                method: id ? "PUT" : "POST",

                headers: getHeaders(),

                body: JSON.stringify(requestData)

            }

        );


        if (!response.ok) {

            throw new Error(
                await getApiError(response)
            );

        }


        closeModal();

        showToast(
            id
                ? "Material request updated successfully."
                : "Material request created successfully.",
            "success"
        );


        await loadRequests();


    } catch (error) {

        console.error(error);

        showToast(
            error.message || "Failed to save request.",
            "error"
        );

    }

}


// ===============================
// EDIT
// ===============================

async function editRequest(id) {

    try {

        const response =
            await fetch(`${API_URL}/${id}`, {
                headers: getHeaders()
            });


        if (!response.ok) {

            throw new Error(
                await getApiError(response)
            );

        }


        const request =
            await response.json();


        document.getElementById("modalTitle")
            .textContent = "Edit Material Request";


        document.getElementById("requestId")
            .value = request.id;


        document.getElementById("requestCode")
            .value = request.requestCode || "";


        document.getElementById("materialName")
            .value = request.materialName || "";


        document.getElementById("quantity")
            .value = request.quantity || "";


        document.getElementById("unit")
            .value = request.unit || "";


        document.getElementById("projectName")
            .value = request.projectName || "";


        document.getElementById("requestedBy")
            .value = request.requestedBy || "";


        document.getElementById("requiredDate")
            .value = request.requiredDate || "";


        document.getElementById("reason")
            .value = request.reason || "";


        document.getElementById("status")
            .value = request.status || "PENDING";


        document.getElementById("requestModal")
            .classList.add("show");


    } catch (error) {

        showToast(
            error.message || "Failed to load request.",
            "error"
        );

    }

}


// ===============================
// VIEW
// ===============================

async function viewRequest(id) {

    const request =
        requests.find(r => r.id === id);


    if (!request) {
        return;
    }


    alert(

        "Material Request\n\n" +

        "Request Code: " + request.requestCode + "\n" +

        "Material: " + request.materialName + "\n" +

        "Quantity: " + request.quantity + " " + request.unit + "\n" +

        "Project: " + request.projectName + "\n" +

        "Requested By: " + request.requestedBy + "\n" +

        "Required Date: " + request.requiredDate + "\n" +

        "Status: " + request.status + "\n\n" +

        "Reason: " + (request.reason || "N/A")

    );

}


// ===============================
// DELETE
// ===============================

async function deleteRequest(id) {

    const request =
        requests.find(r => r.id === id);


    if (!request) {
        return;
    }


    const confirmed =
        confirm(
            `Delete material request ${request.requestCode}?`
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(`${API_URL}/${id}`, {

                method: "DELETE",

                headers: getHeaders()

            });


        if (!response.ok) {

            throw new Error(
                await getApiError(response)
            );

        }


        showToast(
            "Material request deleted successfully.",
            "success"
        );


        await loadRequests();


    } catch (error) {

        showToast(
            error.message || "Failed to delete request.",
            "error"
        );

    }

}


// ===============================
// STATUS STYLE
// ===============================

function getStatusClass(status) {

    switch (status) {

        case "PENDING":
            return "pending";

        case "APPROVED":
            return "approved";

        case "REJECTED":
            return "rejected";

        case "FULFILLED":
            return "fulfilled";

        default:
            return "";

    }

}


// ===============================
// API ERROR
// ===============================

async function getApiError(response) {

    try {

        const contentType =
            response.headers.get("content-type") || "";


        if (contentType.includes("application/json")) {

            const data =
                await response.json();

            return (
                data.message ||
                data.error ||
                "Request failed."
            );

        }


        const text =
            await response.text();

        return text || "Request failed.";


    } catch {

        return "Request failed.";

    }

}


// ===============================
// HTML ESCAPE
// ===============================

function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");

}


// ===============================
// TOAST
// ===============================

function showToast(message, type = "success") {

    const toast =
        document.getElementById("toast");


    toast.textContent = message;

    toast.className = type;

    toast.classList.add("show");


    setTimeout(() => {

        toast.classList.remove("show");

    }, 3000);

}


// Close modal when clicking outside

window.addEventListener("click", event => {

    const modal =
        document.getElementById("requestModal");


    if (event.target === modal) {
        closeModal();
    }

});