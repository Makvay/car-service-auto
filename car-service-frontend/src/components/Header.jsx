import { useEffect, useMemo, useState } from "react";
import PropTypes from "prop-types";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

function formatSeconds(totalSeconds) {
  if (totalSeconds == null || Number.isNaN(totalSeconds)) return "—";
  const s = Math.max(0, Math.floor(totalSeconds));
  const mm = String(Math.floor(s / 60)).padStart(2, "0");
  const ss = String(s % 60).padStart(2, "0");
  return `${mm}:${ss}`;
}

function parseJwt(token) {
  if (!token) return null;
  try {
    const parts = token.split(".");
    if (parts.length !== 3) return null;
    return JSON.parse(atob(parts[1]));
  } catch {
    return null;
  }
}

function getTokenExpiryMs() {
  const expires = localStorage.getItem("kc_token_expires");
  if (!expires) return null;
  return parseInt(expires);
}

export default function Header({ title }) {
  const { t, i18n } = useTranslation();
  const navigate = useNavigate();
  
  const token = localStorage.getItem("kc_token");
  const tokenParsed = useMemo(() => parseJwt(token), [token]);
  const expMs = useMemo(() => {
    const exp = getTokenExpiryMs();
    if (!exp) return null;
    return exp;
  }, []);

  const [now, setNow] = useState(Date.now());

  useEffect(() => {
    const id = setInterval(() => setNow(Date.now()), 1000);
    return () => clearInterval(id);
  }, []);

  const secondsLeft = useMemo(() => {
    if (!expMs) return null;
    return (expMs - now) / 1000;
  }, [expMs, now]);

  const jwtLabel = secondsLeft != null && secondsLeft <= 0 
    ? t("sessionExpired") 
    : formatSeconds(secondsLeft);

  const email = tokenParsed?.email ?? tokenParsed?.preferred_username ?? "—";

  const toggleLanguage = () => {
    const newLang = i18n.language === "ru" ? "en" : "ru";
    i18n.changeLanguage(newLang);
    localStorage.setItem("language", newLang);
  };

  const handleLogout = () => {
    localStorage.removeItem("kc_token");
    localStorage.removeItem("kc_refresh_token");
    localStorage.removeItem("kc_token_expires");
    navigate("/login");
  };

  useEffect(() => {
    const savedLang = localStorage.getItem("language");
    if (savedLang) {
      i18n.changeLanguage(savedLang);
    }
  }, [i18n]);

  return (
    <header className="sticky top-0 z-10 bg-white border-b border-black/10">
      <div className="flex items-center justify-between px-6 py-4">
        <div className="flex items-center gap-3">
          <div className="h-9 w-9 rounded-xl bg-orange text-black flex items-center justify-center font-bold">
            CS
          </div>
          <div>
            <div className="text-sm text-black/60">Car Service</div>
            <div className="text-lg font-semibold leading-tight">{title}</div>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <button
            type="button"
            onClick={toggleLanguage}
            className="text-xs font-medium text-black/60 hover:text-black"
          >
            {i18n.language === "ru" ? "EN" : "RU"}
          </button>

          <div className="hidden sm:block text-right">
            <div className="text-sm font-medium">{email}</div>
            <div className="text-xs text-black/60">
              {t("session")} <span className="font-medium tabular-nums">{jwtLabel}</span>
            </div>
          </div>

          <button
            type="button"
            onClick={handleLogout}
            className="inline-flex items-center justify-center rounded-xl bg-black text-white px-4 py-2 text-sm font-semibold hover:bg-black/90 active:bg-black/80"
          >
            {t("logout")}
          </button>
        </div>
      </div>
    </header>
  );
}

Header.propTypes = {
  title: PropTypes.string
};

Header.defaultProps = {
  title: "Dashboard"
};