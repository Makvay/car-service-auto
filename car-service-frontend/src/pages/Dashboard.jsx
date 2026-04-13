import { useState } from "react";
import { useTranslation } from "react-i18next";
import KPICards from "../components/dashboard/KPICards";
import Charts from "../components/dashboard/Charts";
import RecentClaims from "../components/dashboard/RecentClaims";
import HelpPanel from "../components/HelpPanel";

export default function Dashboard() {
  const { t } = useTranslation();

  return (
    <div>
      <HelpPanel />
      <KPICards />
      <Charts />
      <RecentClaims />
    </div>
  );
}
