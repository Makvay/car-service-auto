import KPICards from "../components/dashboard/KPICards";
import Charts from "../components/dashboard/Charts";
import RecentClaims from "../components/dashboard/RecentClaims";
import HelpPanel from "../components/HelpPanel";

export default function Dashboard() {
  return (
    <div>
      <HelpPanel />
      <KPICards />
      <Charts />
      <RecentClaims />
    </div>
  );
}
