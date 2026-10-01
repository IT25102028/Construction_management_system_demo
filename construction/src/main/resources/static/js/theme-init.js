/* =========================================================
   WBCMS - THEME MANAGER INITIALIZER
   Immediate execution on head load to prevent theme flash
========================================================= */
(function() {
    try {
        const savedTheme = localStorage.getItem("wbcms_theme") || "dark";
        if (savedTheme === "light") {
            document.documentElement.setAttribute("data-theme", "light");
            if (document.body) {
                document.body.classList.add("theme-light");
            } else {
                window.addEventListener("DOMContentLoaded", function() {
                    document.body.classList.add("theme-light");
                });
            }
        } else {
            document.documentElement.setAttribute("data-theme", "dark");
            if (document.body) {
                document.body.classList.remove("theme-light");
            }
        }
    } catch (e) {
        console.warn("Theme init error:", e);
    }
})();

function applyThemeGlobal(theme) {
    localStorage.setItem("wbcms_theme", theme);
    if (theme === "light") {
        document.documentElement.setAttribute("data-theme", "light");
        document.body.classList.add("theme-light");
    } else {
        document.documentElement.setAttribute("data-theme", "dark");
        document.body.classList.remove("theme-light");
    }
}
