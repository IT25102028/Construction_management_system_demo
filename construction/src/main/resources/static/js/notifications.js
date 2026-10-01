/* =========================================================
   WBCMS - UNIVERSAL NOTIFICATION ENGINE
   Real-Time Telemetry & Automated Read/Unread State Machine
========================================================= */

(function () {
    const NOTIF_AUTH_TOKEN = localStorage.getItem("wbcms_token");
    if (!NOTIF_AUTH_TOKEN) return;

    let notificationsList = [];
    let currentFilter = "all"; // "all" | "unread"
    let isPanelOpen = false;

    // Inject styles for notification dropdown panel
    const styleEl = document.createElement("style");
    styleEl.textContent = `
        /* Notification Bell Badge */
        .notification-button {
            position: relative !important;
            cursor: pointer !important;
        }

        .notification-badge {
            position: absolute !important;
            top: -4px !important;
            right: -4px !important;
            background: linear-gradient(135deg, #ef4444, #dc2626) !important;
            color: #ffffff !important;
            font-size: 10.5px !important;
            font-weight: 700 !important;
            min-width: 18px !important;
            height: 18px !important;
            border-radius: 999px !important;
            display: none;
            align-items: center !important;
            justify-content: center !important;
            padding: 0 4px !important;
            box-shadow: 0 0 10px rgba(239, 68, 68, 0.6), 0 0 0 2px rgba(10, 17, 32, 0.95) !important;
            animation: pulse-badge 2s infinite ease-in-out;
            pointer-events: none;
            line-height: 1;
        }

        .notification-badge.active {
            display: inline-flex !important;
        }

        @keyframes pulse-badge {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); box-shadow: 0 0 14px rgba(239, 68, 68, 0.85), 0 0 0 2px rgba(10, 17, 32, 0.95); }
        }

        /* Notification Dropdown Container */
        .notification-dropdown {
            position: absolute;
            top: 56px;
            right: 0;
            width: 380px;
            max-width: calc(100vw - 32px);
            background: rgba(13, 22, 41, 0.97);
            backdrop-filter: blur(28px);
            -webkit-backdrop-filter: blur(28px);
            border: 1px solid rgba(255, 255, 255, 0.14);
            border-radius: 16px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.75), 0 0 0 1px rgba(255, 255, 255, 0.05);
            z-index: 1100;
            display: none;
            flex-direction: column;
            overflow: hidden;
            animation: notifSlideDown 0.22s cubic-bezier(0.16, 1, 0.3, 1);
        }

        .notification-dropdown.open {
            display: flex;
        }

        @keyframes notifSlideDown {
            from { opacity: 0; transform: translateY(-10px) scale(0.97); }
            to { opacity: 1; transform: translateY(0) scale(1); }
        }

        .notif-header {
            padding: 14px 18px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 1px solid rgba(255, 255, 255, 0.08);
            background: rgba(255, 255, 255, 0.02);
        }

        .notif-header h4 {
            margin: 0;
            font-size: 14px;
            font-weight: 700;
            color: #ffffff;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .notif-unread-count-pill {
            background: rgba(239, 68, 68, 0.2);
            border: 1px solid rgba(239, 68, 68, 0.4);
            color: #f87171;
            font-size: 10px;
            font-weight: 700;
            padding: 2px 7px;
            border-radius: 999px;
            letter-spacing: 0.02em;
        }

        .notif-mark-all-btn {
            background: none;
            border: none;
            color: var(--orange, #ff7a1a);
            font-size: 12px;
            font-weight: 600;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 4px 6px;
            border-radius: 6px;
            transition: all 0.15s ease;
        }

        .notif-mark-all-btn:hover {
            background: rgba(255, 122, 26, 0.12);
            color: #ff9944;
        }

        .notif-tabs {
            display: flex;
            border-bottom: 1px solid rgba(255, 255, 255, 0.06);
            padding: 6px 14px;
            gap: 6px;
            background: rgba(9, 16, 32, 0.6);
        }

        .notif-tab {
            background: none;
            border: none;
            color: #94a3b8;
            font-size: 12px;
            font-weight: 600;
            padding: 5px 12px;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.15s ease;
        }

        .notif-tab.active {
            background: rgba(255, 255, 255, 0.08);
            color: #ffffff;
        }

        .notif-list {
            max-height: 380px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
        }

        .notif-list::-webkit-scrollbar {
            width: 5px;
        }
        .notif-list::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.15);
            border-radius: 4px;
        }

        .notif-item {
            padding: 12px 16px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
            display: flex;
            align-items: flex-start;
            gap: 12px;
            cursor: pointer;
            transition: background 0.15s ease;
            position: relative;
        }

        .notif-item:hover {
            background: rgba(255, 255, 255, 0.04);
        }

        .notif-item.unread {
            background: rgba(255, 122, 26, 0.06);
        }

        .notif-item.unread:hover {
            background: rgba(255, 122, 26, 0.1);
        }

        .notif-icon-box {
            width: 32px;
            height: 32px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            flex-shrink: 0;
            margin-top: 2px;
        }

        .notif-icon-box.task {
            background: rgba(56, 189, 248, 0.15);
            color: #38bdf8;
            border: 1px solid rgba(56, 189, 248, 0.3);
        }

        .notif-icon-box.material {
            background: rgba(251, 191, 36, 0.15);
            color: #fbbf24;
            border: 1px solid rgba(251, 191, 36, 0.3);
        }

        .notif-icon-box.document {
            background: rgba(168, 85, 247, 0.15);
            color: #c084fc;
            border: 1px solid rgba(168, 85, 247, 0.3);
        }

        .notif-icon-box.system {
            background: rgba(239, 68, 68, 0.15);
            color: #f87171;
            border: 1px solid rgba(239, 68, 68, 0.3);
        }

        .notif-content {
            flex: 1;
            min-width: 0;
        }

        .notif-item-top {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 8px;
            margin-bottom: 3px;
        }

        .notif-title {
            font-size: 12.5px;
            font-weight: 600;
            color: #f8fafc;
            line-height: 1.3;
        }

        .notif-item.unread .notif-title {
            color: #ffffff;
            font-weight: 700;
        }

        .notif-unseen-tag {
            background: rgba(239, 68, 68, 0.2);
            color: #f87171;
            font-size: 9.5px;
            font-weight: 700;
            padding: 1px 6px;
            border-radius: 4px;
            border: 1px solid rgba(239, 68, 68, 0.35);
            text-transform: uppercase;
            letter-spacing: 0.04em;
            flex-shrink: 0;
        }

        .notif-seen-tag {
            color: #64748b;
            font-size: 10px;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 3px;
            flex-shrink: 0;
        }

        .notif-desc {
            font-size: 11.5px;
            color: #94a3b8;
            line-height: 1.4;
            margin: 0 0 5px 0;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        .notif-time {
            font-size: 10px;
            color: #64748b;
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .notif-empty {
            padding: 35px 20px;
            text-align: center;
            color: #64748b;
        }

        .notif-empty i {
            font-size: 30px;
            color: #475569;
            display: block;
            margin-bottom: 8px;
        }

        .notif-footer {
            padding: 10px 16px;
            border-top: 1px solid rgba(255, 255, 255, 0.06);
            background: rgba(255, 255, 255, 0.02);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .notif-footer span {
            font-size: 11px;
            color: #64748b;
        }

        .notif-clear-btn {
            background: none;
            border: none;
            color: #94a3b8;
            font-size: 11px;
            font-weight: 500;
            cursor: pointer;
            padding: 2px 6px;
            border-radius: 4px;
            transition: color 0.15s ease;
        }

        .notif-clear-btn:hover {
            color: #f87171;
        }
    `;
    document.head.appendChild(styleEl);

    // Initial boot on DOM ready
    document.addEventListener("DOMContentLoaded", function () {
        setupNotificationDOM();
        fetchNotifications();
        // Poll for new notifications every 20 seconds
        setInterval(fetchNotifications, 20000);
    });

    function getAuthHeaders() {
        return {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + NOTIF_AUTH_TOKEN
        };
    }

    function setupNotificationDOM() {
        const bellBtn = document.querySelector(".notification-button");
        if (!bellBtn) return;

        // Ensure badge container exists
        let badgeEl = bellBtn.querySelector(".notification-badge");
        if (!badgeEl) {
            badgeEl = document.createElement("span");
            badgeEl.className = "notification-badge";
            badgeEl.id = "globalNotifBadge";
            bellBtn.appendChild(badgeEl);
        }

        // Wrap bell button in relative wrapper if needed
        const headerRight = bellBtn.parentElement;
        if (headerRight) {
            headerRight.style.position = "relative";
        }

        // Create dropdown popover
        let dropdown = document.getElementById("notificationDropdownPanel");
        if (!dropdown) {
            dropdown = document.createElement("div");
            dropdown.className = "notification-dropdown";
            dropdown.id = "notificationDropdownPanel";
            dropdown.innerHTML = `
                <div class="notif-header">
                    <h4>
                        <i class="bi bi-bell-fill" style="color:var(--orange, #ff7a1a);"></i>
                        Notifications
                        <span class="notif-unread-count-pill" id="notifHeaderCount">0 New</span>
                    </h4>
                    <button class="notif-mark-all-btn" id="notifMarkAllBtn" title="Mark all notifications as seen">
                        <i class="bi bi-check2-all"></i> Mark all read
                    </button>
                </div>
                <div class="notif-tabs">
                    <button class="notif-tab active" data-filter="all">All (<span id="notifTotalCount">0</span>)</button>
                    <button class="notif-tab" data-filter="unread">Unread Only (<span id="notifUnreadTabCount">0</span>)</button>
                </div>
                <div class="notif-list" id="notifListContainer">
                    <div class="notif-empty">
                        <i class="bi bi-bell-slash"></i>
                        <span>Loading notifications...</span>
                    </div>
                </div>
                <div class="notif-footer">
                    <span><i class="bi bi-shield-check" style="color:#34d399;"></i> Real-Time Sync</span>
                    <button class="notif-clear-btn" id="notifClearReadBtn">Clear Read</button>
                </div>
            `;
            if (headerRight) {
                headerRight.appendChild(dropdown);
            } else {
                document.body.appendChild(dropdown);
            }
        }

        // Bell click toggle
        bellBtn.addEventListener("click", function (e) {
            e.stopPropagation();
            toggleNotificationPanel();
        });

        // Tab switches
        dropdown.querySelectorAll(".notif-tab").forEach(tab => {
            tab.addEventListener("click", function (e) {
                e.stopPropagation();
                dropdown.querySelectorAll(".notif-tab").forEach(t => t.classList.remove("active"));
                this.classList.add("active");
                currentFilter = this.getAttribute("data-filter");
                renderNotificationList();
            });
        });

        // Mark all read button
        const markAllBtn = document.getElementById("notifMarkAllBtn");
        if (markAllBtn) {
            markAllBtn.addEventListener("click", function (e) {
                e.stopPropagation();
                markAllNotificationsAsRead();
            });
        }

        // Clear read button
        const clearBtn = document.getElementById("notifClearReadBtn");
        if (clearBtn) {
            clearBtn.addEventListener("click", function (e) {
                e.stopPropagation();
                clearReadNotifications();
            });
        }

        // Close on click outside
        document.addEventListener("click", function (e) {
            if (isPanelOpen && dropdown && !dropdown.contains(e.target) && !bellBtn.contains(e.target)) {
                closeNotificationPanel();
            }
        });
    }

    function toggleNotificationPanel() {
        const dropdown = document.getElementById("notificationDropdownPanel");
        if (!dropdown) return;
        isPanelOpen = !isPanelOpen;
        dropdown.classList.toggle("open", isPanelOpen);
        if (isPanelOpen) {
            renderNotificationList();
        }
    }

    function closeNotificationPanel() {
        const dropdown = document.getElementById("notificationDropdownPanel");
        if (dropdown) dropdown.classList.remove("open");
        isPanelOpen = false;
    }

    async function fetchNotifications() {
        try {
            const res = await fetch("/api/notifications/all", { headers: getAuthHeaders() });
            if (res.ok) {
                const data = await res.json();
                notificationsList = data || [];
                updateBadgeCounters();
                if (isPanelOpen) {
                    renderNotificationList();
                }
            }
        } catch (err) {
            console.warn("Notification fetch warning:", err);
        }
    }

    function updateBadgeCounters() {
        const unreadCount = notificationsList.filter(n => !n.readFlag).length;
        const totalCount = notificationsList.length;

        // Update all header badges on page
        document.querySelectorAll(".notification-badge").forEach(badge => {
            if (unreadCount > 0) {
                badge.textContent = unreadCount > 99 ? "99+" : unreadCount;
                badge.classList.add("active");
            } else {
                badge.textContent = "0";
                badge.classList.remove("active");
            }
        });

        const headerCount = document.getElementById("notifHeaderCount");
        const totalCountEl = document.getElementById("notifTotalCount");
        const unreadTabCount = document.getElementById("notifUnreadTabCount");

        if (headerCount) headerCount.textContent = `${unreadCount} New`;
        if (totalCountEl) totalCountEl.textContent = totalCount;
        if (unreadTabCount) unreadTabCount.textContent = unreadCount;
    }

    function renderNotificationList() {
        const container = document.getElementById("notifListContainer");
        if (!container) return;

        let filtered = notificationsList;
        if (currentFilter === "unread") {
            filtered = notificationsList.filter(n => !n.readFlag);
        }

        if (filtered.length === 0) {
            container.innerHTML = `
                <div class="notif-empty">
                    <i class="bi bi-check2-circle" style="color:#34d399; font-size:28px;"></i>
                    <p style="margin:4px 0 0 0; font-size:12.5px; color:#cbd5e1;">All caught up!</p>
                    <span style="font-size:11px;">No ${currentFilter === "unread" ? "unread " : ""}notifications found.</span>
                </div>
            `;
            return;
        }

        container.innerHTML = "";
        filtered.forEach(n => {
            const isUnread = !n.readFlag;
            const type = (n.type || "SYSTEM").toLowerCase();
            const timeAgo = formatTimeAgo(n.createdAt);

            let iconClass = "bi-bell";
            let boxClass = "system";

            if (type.includes("task")) {
                iconClass = "bi-clipboard-check";
                boxClass = "task";
            } else if (type.includes("material")) {
                iconClass = "bi-box-seam";
                boxClass = "material";
            } else if (type.includes("document") || type.includes("blueprint")) {
                iconClass = "bi-file-earmark-text";
                boxClass = "document";
            } else if (type.includes("issue") || type.includes("alert")) {
                iconClass = "bi-exclamation-octagon";
                boxClass = "system";
            }

            const item = document.createElement("div");
            item.className = `notif-item ${isUnread ? "unread" : "seen"}`;
            item.innerHTML = `
                <div class="notif-icon-box ${boxClass}">
                    <i class="bi ${iconClass}"></i>
                </div>
                <div class="notif-content">
                    <div class="notif-item-top">
                        <span class="notif-title">${escapeHtml(n.title)}</span>
                        ${isUnread ? '<span class="notif-unseen-tag">New</span>' : '<span class="notif-seen-tag"><i class="bi bi-check2"></i> Seen</span>'}
                    </div>
                    <p class="notif-desc">${escapeHtml(n.message)}</p>
                    <div class="notif-time">
                        <i class="bi bi-clock"></i> ${escapeHtml(timeAgo)}
                    </div>
                </div>
            `;

            // When user clicks the notification, automatically mark as seen / read
            item.addEventListener("click", async function () {
                if (isUnread) {
                    await markNotificationAsRead(n.id);
                }
            });

            container.appendChild(item);
        });
    }

    async function markNotificationAsRead(id) {
        // Optimistic UI update
        const notif = notificationsList.find(n => n.id === id);
        if (notif) {
            notif.readFlag = true;
        }
        updateBadgeCounters();
        renderNotificationList();

        try {
            const res = await fetch(`/api/notifications/${id}/read`, {
                method: "POST",
                headers: getAuthHeaders()
            });
            if (!res.ok) {
                // Fallback to PATCH
                await fetch(`/api/notifications/${id}/read`, {
                    method: "PATCH",
                    headers: getAuthHeaders()
                });
            }
        } catch (err) {
            console.warn("Could not mark notification read:", err);
        }
    }

    async function markAllNotificationsAsRead() {
        // Optimistic update
        notificationsList.forEach(n => { n.readFlag = true; });
        updateBadgeCounters();
        renderNotificationList();

        try {
            await fetch("/api/notifications/mark-all-read", {
                method: "POST",
                headers: getAuthHeaders()
            });
        } catch (err) {
            console.warn("Could not mark all notifications read:", err);
        }
    }

    async function clearReadNotifications() {
        try {
            await fetch("/api/notifications/clear-all", {
                method: "DELETE",
                headers: getAuthHeaders()
            });
            notificationsList = notificationsList.filter(n => !n.readFlag);
            updateBadgeCounters();
            renderNotificationList();
        } catch (err) {
            console.warn("Could not clear notifications:", err);
        }
    }

    function formatTimeAgo(isoString) {
        if (!isoString) return "Recently";
        const date = new Date(isoString);
        const now = new Date();
        const seconds = Math.floor((now - date) / 1000);

        if (seconds < 60) return "Just now";
        const minutes = Math.floor(seconds / 60);
        if (minutes < 60) return `${minutes}m ago`;
        const hours = Math.floor(minutes / 60);
        if (hours < 24) return `${hours}h ago`;
        const days = Math.floor(hours / 24);
        if (days < 7) return `${days}d ago`;
        return date.toLocaleDateString();
    }

    function escapeHtml(text) {
        if (!text) return "";
        return String(text)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    // Expose functions globally if needed
    window.WBCMS_Notifications = {
        fetch: fetchNotifications,
        markRead: markNotificationAsRead,
        markAllRead: markAllNotificationsAsRead
    };
})();
