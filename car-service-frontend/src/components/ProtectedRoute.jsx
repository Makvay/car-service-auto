import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import PropTypes from "prop-types";

function isAuthenticated() {
  const token = localStorage.getItem("kc_token");
  const expires = localStorage.getItem("kc_token_expires");
  
  if (!token || !expires) return false;
  if (Date.now() > parseInt(expires)) {
    return false;
  }
  return true;
}

export default function ProtectedRoute({ children }) {
  const [status, setStatus] = useState("init");
  const navigate = useNavigate();

  useEffect(() => {
    const checkAuth = () => {
      if (!isAuthenticated()) {
        navigate("/login", { replace: true });
      } else {
        setStatus("ready");
      }
    };
    
    checkAuth();
  }, [navigate]);

  if (status === "init") {
    return (
      <div className="min-h-screen bg-gray-900 flex items-center justify-center">
        <div className="text-center">
          <div className="w-12 h-12 border-4 border-orange border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-400">Проверка авторизации...</p>
        </div>
      </div>
    );
  }

  if (status !== "ready") {
    return null;
  }

  return children;
}

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired
};