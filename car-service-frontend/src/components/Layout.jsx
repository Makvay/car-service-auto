import { NavLink, useLocation } from "react-router-dom";
import PropTypes from "prop-types";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import Header from "./Header";
import HelpPanel from "./HelpPanel";

function Icon({ path }) {
  return (
    <svg
      viewBox="0 0 24 24"
      className="h-5 w-5"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      <path d={path} />
    </svg>
  );
}

Icon.propTypes = { path: PropTypes.string.isRequired };

const iconPaths = {
  dashboard: "M3 13h8V3H3v10Zm10 8h8V11h-8v10ZM3 21h8V15H3v6Zm10-8h8V3h-8v10Z",
  claims: "M9 12h6M9 16h6M7 3h10a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2Z",
  clients:
    "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm13 10v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75",
  masters: "M20 21v-2a4 4 0 0 0-3-3.87M4 21v-2a4 4 0 0 1 4-4h4a4 4 0 0 1 4 4v2M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8Z",
  warehouse: "M3 9.5 12 4l9 5.5V20a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5Z",
  notifications: "M18 8a6 6 0 0 0-12 0c0 7-3 7-3 7h18s-3 0-3-7Zm-7 13a2 2 0 0 0 4 0",
  nsi: "M4 19h16M4 5h16M7 9h10M7 13h10",
  help: "M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10ZM12 16v-4M12 8h.01"
};

export default function Layout({ children }) {
  const { t } = useTranslation();
  const location = useLocation();
  const [showHelp, setShowHelp] = useState(false);

  const items = useMemo(
    () => [
      { to: "/", label: t("dashboard"), icon: "dashboard" },
      { to: "/claims", label: t("claims"), icon: "claims" },
      { to: "/clients", label: t("clients"), icon: "clients" },
      { to: "/masters", label: t("masters"), icon: "masters" },
      { to: "/warehouse", label: t("warehouse"), icon: "warehouse" },
      { to: "/notifications", label: t("notifications"), icon: "notifications" },
      { to: "/nsi", label: t("nsi"), icon: "nsi" }
    ],
    [t]
  );

  const title = useMemo(() => {
    const item = items.find((x) => x.to === location.pathname);
    return item?.label ?? t("dashboard");
  }, [items, location.pathname, t]);

  return (
    <div className="min-h-screen bg-white text-black flex">
      <aside className="w-72 bg-black text-white flex flex-col">
        <div className="px-6 py-6 border-b border-white/10">
          <div className="text-sm text-white/70">B2B Dashboard</div>
          <div className="mt-1 text-xl font-semibold">Car Service</div>
        </div>

        <nav className="px-3 py-4 flex-1">
          <div className="space-y-1">
            {items.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  [
                    "group flex items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium",
                    isActive
                      ? "bg-white/10 text-white"
                      : "text-white/80 hover:text-white hover:bg-white/5"
                  ].join(" ")
                }
                end={item.to === "/"}
              >
                <span className="text-orange">
                  <Icon path={iconPaths[item.icon]} />
                </span>
                <span>{item.label}</span>
              </NavLink>
            ))}
            <button
              onClick={() => setShowHelp(!showHelp)}
              className="w-full group flex items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium text-white/80 hover:text-white hover:bg-white/5"
            >
              <span className="text-orange">
                <Icon path={iconPaths.help} />
              </span>
              <span>{showHelp ? t("hideHelp") : t("documentation")}</span>
            </button>
          </div>
        </nav>

        <div className="px-6 py-4 border-t border-white/10 text-xs text-white/60">
          Gateway: <span className="text-white/80">localhost:8081</span>
        </div>
      </aside>

      <div className="flex-1 flex flex-col min-w-0">
        <Header title={showHelp ? t("documentation") : title} />
        <main className="p-6">
          {showHelp ? <HelpPanel /> : children}
        </main>
      </div>
    </div>
  );
}

Layout.propTypes = {
  children: PropTypes.node.isRequired
};
