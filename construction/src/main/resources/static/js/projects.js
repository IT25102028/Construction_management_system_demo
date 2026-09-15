const token =
    localStorage.getItem("wbcms_token");

if (!token) {
    window.location.href = "/login.html";
}

let allProjects = [];

let currentStatus = "ALL";

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const user =
            JSON.parse(
                localStorage.getItem(
                    "wbcms_user"
                ) || "{}"
            );

        document.getElementById(
            "userName"
        ).textContent =
            user.username ||
            "Staff User";

        await loadProjects();

        document.getElementById(
            "projectSearch"
        ).addEventListener(
            "input",
            renderProjects
        );

        document
            .querySelectorAll(".filter")
            .forEach(button => {

                button.addEventListener(
                    "click",
                    () => {

                        document
                            .querySelectorAll(
                                ".filter"
                            )
                            .forEach(btn =>
                                btn.classList
                                    .remove(
                                        "active"
                                    )
                            );

                        button.classList
                            .add("active");

                        currentStatus =
                            button.dataset.status;

                        renderProjects();
                    }
                );
            });

        document.getElementById(
            "newProjectBtn"
        ).addEventListener(
            "click",
            () => {

                alert(
                    "Next we will connect this button to POST /api/projects."
                );

            }
        );
    }
);


async function loadProjects() {

    const grid =
        document.getElementById(
            "projectsGrid"
        );

    try {

        const response =
            await fetch(
                "/api/projects?page=0&size=50",
                {
                    headers: {
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {

            throw new Error(
                "Could not load projects."
            );

        }

        const data =
            await response.json();

        allProjects =
            data.content || data;

        renderProjects();

    } catch (error) {

        grid.innerHTML = `
            <div class="loading">
                ${error.message}
            </div>
        `;
    }
}


function renderProjects() {

    const search =
        document.getElementById(
            "projectSearch"
        ).value.toLowerCase();


    const projects =
        allProjects.filter(project => {

            const status =
                String(
                    project.status || ""
                ).toUpperCase();


            const text = `
                ${project.name || ""}
                ${project.location || ""}
                ${project.clientName || ""}
            `.toLowerCase();


            return (
                (
                    currentStatus === "ALL" ||
                    status === currentStatus
                ) &&
                text.includes(search)
            );
        });


    const grid =
        document.getElementById(
            "projectsGrid"
        );


    grid.innerHTML =
        projects.map(
            (project, index) => {

                const photos = [
                    "photo-1",
                    "photo-2",
                    "photo-3"
                ];


                const photo =
                    photos[
                    index % photos.length
                        ];


                const status =
                    String(
                        project.status ||
                        "PLANNING"
                    ).toUpperCase();


                const statusClass =
                    status === "PLANNING"
                        ? "planning"
                        : "active";


                return `

                <article class="admin-project">

                    <div
                        class="project-photo ${photo}">
                    </div>

                    <div
                        class="admin-project-body">

                        <span
                            class="status-badge ${statusClass}">

                            ${escapeHtml(
                    project.status ||
                    "PLANNING"
                )}

                        </span>

                        <h3>

                            ${escapeHtml(
                    project.name ||
                    "Untitled Project"
                )}

                        </h3>

                        <p>

                            ${escapeHtml(
                    project.location ||
                    "Location not set"
                )}

                            ·

                            ${escapeHtml(
                    project.clientName ||
                    "Client not set"
                )}

                        </p>

                        <div
                            class="progress-row">

                            <div
                                class="progress-bar">

                                <i
                                    style="
                                    width:${Number(
                    project.completionPercentage ||
                    0
                )}%;
                                    ">
                                </i>

                            </div>

                            <span>

                                ${Number(
                    project.completionPercentage ||
                    0
                )}%

                            </span>

                        </div>

                        <div class="admin-meta">

                            <span>
                                Budget
                            </span>

                            <b>

                                ${
                    project.budget
                        ? "LKR " +
                        Number(
                            project.budget
                        ).toLocaleString()
                        : "Not set"
                }

                            </b>

                        </div>

                    </div>

                </article>

                `;
            }
        )
            .join("");


    if (!projects.length) {

        grid.innerHTML = `
            <div class="loading">
                No matching projects.
            </div>
        `;

    }
}


function escapeHtml(value) {

    return String(value).replace(
        /[&<>"']/g,

        character => ({

            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "'": "&#039;"

        }[character])
    );
}