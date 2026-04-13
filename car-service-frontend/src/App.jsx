import { Navigate, createBrowserRouter, RouterProvider } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout from "./components/Layout";
import Dashboard from "./pages/Dashboard";
import Claims from "./pages/Claims";
import Clients from "./pages/Clients";
import Masters from "./pages/Masters";
import Warehouse from "./pages/Warehouse";
import Notifications from "./pages/Notifications";
import NSI from "./pages/NSI";
import Login from "./pages/Login";

const router = createBrowserRouter(
  [
    {
      path: "/login",
      element: <Login />
    },
    {
      path: "/",
      element: (
        <ProtectedRoute>
          <Layout>
            <Dashboard />
          </Layout>
        </ProtectedRoute>
      )
    },
    {
      path: "/claims",
      element: (
        <ProtectedRoute>
          <Layout>
            <Claims />
          </Layout>
        </ProtectedRoute>
      )
    },
    {
      path: "/clients",
      element: (
        <ProtectedRoute>
          <Layout>
            <Clients />
          </Layout>
        </ProtectedRoute>
      )
    },
    {
      path: "/masters",
      element: (
        <ProtectedRoute>
          <Layout>
            <Masters />
          </Layout>
        </ProtectedRoute>
      )
    },
    {
      path: "/warehouse",
      element: (
        <ProtectedRoute>
          <Layout>
            <Warehouse />
          </Layout>
        </ProtectedRoute>
      )
    },
    {
      path: "/notifications",
      element: (
        <ProtectedRoute>
          <Layout>
            <Notifications />
          </Layout>
        </ProtectedRoute>
      )
    },
    {
      path: "/nsi",
      element: (
        <ProtectedRoute>
          <Layout>
            <NSI />
          </Layout>
        </ProtectedRoute>
      )
    },
    { path: "*", element: <Navigate to="/" replace /> }
  ],
  {
    future: {
      v7_startTransition: true,
      v7_relativeSplatPath: true
    }
  }
);

export default function App() {
  return <RouterProvider router={router} />;
}