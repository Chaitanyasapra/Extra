// src/pages/SignUpPage.jsx
import React, { useState, useEffect } from "react";
import api from "../api/axios";
import { useNavigate, Link } from "react-router-dom";
import loginBg from "../images/2.jpg";
 
export default function SignUpPage() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');            
  const [role, setRole] = useState('BUYER');    // default value must match backend ENUM  
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');            
  const [locationId, setLocationId] = useState('');  
  const [error, setError] = useState(null);
  const navigate = useNavigate();
 
  const submit = async (e) => {
    e.preventDefault();
    const form = e.currentTarget;
 
    if (!form.checkValidity()) {
      form.reportValidity();
      return;
    }
 
    setError(null);
 
    try {
      // FINAL — Matches backend DTO EXACTLY
      const res = await api.post('/auth/signup', {
        name: username,
        email,
        password,
        phone,
        role,          // ADMIN, SELLER, BUYER
        locationId,    // MUST match backend variable name
      });
 
      const { token, username: uname } = res.data || {};
 
      if (token) localStorage.setItem('token', token);
      if (uname) localStorage.setItem('username', uname);
 
      navigate('/dashboard');
 
    } catch (err) {
      setError(err.response?.data?.error || 'Unable to register. Please try again.');
    }
  };
 
  useEffect(() => {
    if (!error) return;
    const timer = setTimeout(() => setError(null), 3000);
    return () => clearTimeout(timer);
  }, [error]);
 
  return (
    <div className="min-h-screen bg-white flex flex-col items-center justify-center px-4">
     
      {/* TOP TITLE */}
      <div className="text-center mb-6">
        <h2 className="text-xl md:text-2xl font-semibold text-gray-900">
          Create your account
        </h2>
        <p className="text-xs md:text-sm text-gray-500">
          Join ShopSphere — start selling or shopping in minutes.
        </p>
      </div>
 
      {/* CARD */}
      <div
        className="w-full max-w-4xl rounded-2xl shadow-2xl overflow-hidden grid grid-cols-1 md:grid-cols-2"
        style={{ minHeight: 460 }}
      >
        {/* LEFT IMAGE */}
        <div className="bg-white" style={{ height: 460 }}>
          <img
            src={loginBg}
            alt="hero"
            className="w-full h-full object-cover block"
          />
        </div>
 
        {/* RIGHT FORM */}
        <div className="flex items-center justify-center p-6 bg-white">
          <div className="w-full max-w-sm">
 
            <form onSubmit={submit} className="space-y-2">
              <div>
                <label className="block text-xs font-medium text-gray-700">
                  Username
                </label>
                <input
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  className="mt-1 block w-full px-3 py-2 text-sm border border-gray-200 rounded-md focus:outline-none focus:ring-1 focus:ring-gray-300"
                  placeholder="Your display name"
                />
              </div>
 
              <div>
                <label className="block text-xs font-medium text-gray-700">
                  Email
                </label>
                <input
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  type="email"
                  className="mt-1 block w-full px-3 py-2 text-sm border border-gray-200 rounded-md focus:outline-none focus:ring-1 focus:ring-gray-300"
                  placeholder="you@example.com"
                />
              </div>
 
              <div>
                <label className="block text-xs font-medium text-gray-700">
                  Password
                </label>
                <input
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  type="password"
                  className="mt-1 block w-full px-3 py-2 text-sm border border-gray-200 rounded-md focus:outline-none focus:ring-1 focus:ring-gray-300"
                  placeholder="Create a secure password"
                />
              </div>
 
              <div>
                <label className="block text-xs font-medium text-gray-700">
                  Phone
                </label>
                <input
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  required
                  className="mt-1 block w-full px-3 py-2 text-sm border border-gray-200 rounded-md focus:outline-none focus:ring-1 focus:ring-gray-300"
                  placeholder="+91 9xxxxxxxxx"
                />
              </div>
 
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="block text-xs font-medium text-gray-700">
                    Role
                  </label>
                  <select
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                    className="mt-1 block w-full px-2 py-2 text-sm border border-gray-200 rounded-md bg-white"
                  >
                    <option value="BUYER">User</option>
                    <option value="SELLER">Merchant</option>
                  </select>
                </div>
 
                <div>
                  <label className="block text-xs font-medium text-gray-700">
                    Location
                  </label>
                  <select
                    required
                    value={locationId}
                    onChange={(e) => setLocationId(e.target.value)}
                    className="mt-1 block w-full px-2 py-2 text-sm border border-gray-200 rounded-md bg-white"
                  >
                    <option value="">Select a Location</option>
                    <option value="1">Chandigarh</option>
                    <option value="2">Jaipur</option>
                    <option value="3">Pune</option>
                    <option value="4">Bangalore</option>
                  </select>
                </div>
              </div>
 
              <button
                type="submit"
                className="w-full mt-1 inline-flex items-center justify-center px-4 py-2 text-sm bg-black text-white rounded-md hover:opacity-95"
              >
                Register
              </button>
            </form>
 
            <div className="mt-2 text-xs text-gray-600 text-center">
              Already have an account?{" "}
              <Link to="/login" className="text-black font-medium">
                Sign in
              </Link>
            </div>
 
            {error && (
              <div
                role="alert"
                className="mt-2 rounded-md bg-red-50 border border-red-100 p-2 text-xs text-red-700 text-center"
              >
                {error}
              </div>
            )}
 
          </div>
        </div>
      </div>
    </div>
  );
}