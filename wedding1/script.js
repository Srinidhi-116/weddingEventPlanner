// Initialize the application when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {
    // Initialize the app state
    const appState = {
      currentUser: null,
      userRole: null,
      packages: [],
      bookings: [],
      currentPackage: null,
    }
  
    // Load sample data
    loadSampleData()
  
    // Set up event listeners
    setupEventListeners()
  
    // Initialize the UI
    updateUI()
  
    // Function to load sample data
    function loadSampleData() {
      // Sample packages
      appState.packages = [
        {
          id: 1,
          name: "Elegant Affair",
          description:
            "A complete wedding package that includes everything you need for an elegant and memorable wedding day.",
          price: 5000,
          vendorId: 1,
          vendorName: "Luxe Weddings",
          services: ["Photography", "Catering", "Venue", "Decoration"],
          image:
            "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        },
        {
          id: 2,
          name: "Rustic Romance",
          description:
            "A charming rustic-themed wedding package perfect for couples who love the outdoors and natural aesthetics.",
          price: 3500,
          vendorId: 2,
          vendorName: "Country Celebrations",
          services: ["Photography", "Venue", "Decoration", "Music"],
          image:
            "https://images.unsplash.com/photo-1465495976277-4387d4b0b4c6?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        },
        {
          id: 3,
          name: "Urban Chic",
          description: "A modern and stylish wedding package designed for contemporary couples in urban settings.",
          price: 4200,
          vendorId: 3,
          vendorName: "City Weddings",
          services: ["Photography", "Catering", "Makeup", "Music"],
          image:
            "https://images.unsplash.com/photo-1519225421980-715cb0215aed?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        },
        {
          id: 4,
          name: "Intimate Celebration",
          description:
            "Perfect for small weddings, this package focuses on creating an intimate and personal experience.",
          price: 2800,
          vendorId: 1,
          vendorName: "Luxe Weddings",
          services: ["Photography", "Catering", "Decoration"],
          image:
            "https://images.unsplash.com/photo-1509927083803-4bd519298ac4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        },
      ]
  
      // Sample bookings
      appState.bookings = [
        {
          id: 1,
          packageId: 1,
          customerId: 1,
          customerName: "Emma Johnson",
          date: "2025-06-15",
          guests: 120,
          notes: "Would like extra floral arrangements if possible.",
          status: "confirmed",
          createdAt: "2024-12-10",
        },
        {
          id: 2,
          packageId: 3,
          customerId: 2,
          customerName: "Michael Smith",
          date: "2025-08-22",
          guests: 80,
          notes: "Vegetarian menu options required.",
          status: "pending",
          createdAt: "2024-12-15",
        },
      ]
  
      // Save to localStorage
      localStorage.setItem("weddingPackages", JSON.stringify(appState.packages))
      localStorage.setItem("weddingBookings", JSON.stringify(appState.bookings))
    }
  
    // Function to set up event listeners
    function setupEventListeners() {
      // Navigation links
      document.getElementById("home-link").addEventListener("click", (e) => {
        e.preventDefault()
        showSection("home-section")
      })
  
      document.getElementById("login-link").addEventListener("click", (e) => {
        e.preventDefault()
        showSection("login-section")
      })
  
      document.getElementById("register-link").addEventListener("click", (e) => {
        e.preventDefault()
        showSection("register-section")
      })
  
      // Auth form switches
      document.getElementById("switch-to-register").addEventListener("click", (e) => {
        e.preventDefault()
        showSection("register-section")
      })
  
      document.getElementById("switch-to-login").addEventListener("click", (e) => {
        e.preventDefault()
        showSection("login-section")
      })
  
      // Role selector buttons
      const roleButtons = document.querySelectorAll(".role-btn")
      roleButtons.forEach((button) => {
        button.addEventListener("click", function () {
          const container = this.closest(".auth-container")
          container.querySelectorAll(".role-btn").forEach((btn) => {
            btn.classList.remove("active")
          })
          this.classList.add("active")
  
          const role = this.getAttribute("data-role")
          const vendorFields = container.querySelectorAll(".vendor-field")
  
          if (role === "vendor") {
            vendorFields.forEach((field) => (field.style.display = "block"))
          } else {
            vendorFields.forEach((field) => (field.style.display = "none"))
          }
        })
      })
  
      // Login form
      document.getElementById("login-form").addEventListener("submit", (e) => {
        e.preventDefault()
        const email = document.getElementById("login-email").value
        const password = document.getElementById("login-password").value
        const role = document.querySelector(".auth-container .role-btn.active").getAttribute("data-role")
  
        // Simulate login (in a real app, this would validate against a database)
        appState.currentUser = {
          id: role === "vendor" ? 1 : 2,
          name: role === "vendor" ? "Luxe Weddings" : "Emma Johnson",
          email: email,
          role: role,
        }
  
        appState.userRole = role
  
        // Update UI based on role
        if (role === "vendor") {
          showSection("vendor-dashboard")
          updateVendorDashboard()
        } else {
          showSection("customer-dashboard")
          updateCustomerDashboard()
        }
  
        // Update navigation
        updateNavigation()
      })
  
      // Register form
      document.getElementById("register-form").addEventListener("submit", (e) => {
        e.preventDefault()
        const name = document.getElementById("register-name").value
        const email = document.getElementById("register-email").value
        const password = document.getElementById("register-password").value
        const role = document.querySelector("#register-section .role-btn.active").getAttribute("data-role")
  
        // Additional vendor fields
        let businessName = ""
        let businessDescription = ""
  
        if (role === "vendor") {
          businessName = document.getElementById("business-name").value
          businessDescription = document.getElementById("business-description").value
        }
  
        // Simulate registration (in a real app, this would save to a database)
        appState.currentUser = {
          id: Date.now(),
          name: role === "vendor" ? businessName : name,
          email: email,
          role: role,
        }
  
        appState.userRole = role
  
        // Update UI based on role
        if (role === "vendor") {
          showSection("vendor-dashboard")
          updateVendorDashboard()
        } else {
          showSection("customer-dashboard")
          updateCustomerDashboard()
        }
  
        // Update navigation
        updateNavigation()
      })
  
      // Home page buttons
      document.getElementById("find-vendor-btn").addEventListener("click", () => {
        appState.userRole = "customer"
        showSection("customer-dashboard")
        updateCustomerDashboard()
      })
  
      document.getElementById("become-vendor-btn").addEventListener("click", () => {
        showSection("register-section")
        const vendorRoleBtn = document.querySelector('#register-section .role-btn[data-role="vendor"]')
        vendorRoleBtn.click()
      })
  
      // Vendor dashboard
      document.getElementById("add-package-btn").addEventListener("click", () => {
        // Reset form for new package
        document.getElementById("package-form-title").textContent = "Add New Package"
        document.getElementById("package-form").reset()
        document.getElementById("package-id").value = ""
  
        showSection("package-form-section")
      })
  
      // Package form
      document.getElementById("package-form").addEventListener("submit", (e) => {
        e.preventDefault()
  
        const packageId = document.getElementById("package-id").value
        const isEditing = packageId !== ""
  
        // Get form values
        const name = document.getElementById("package-name").value
        const description = document.getElementById("package-description").value
        const price = Number.parseFloat(document.getElementById("package-price").value)
        const image =
          document.getElementById("package-image").value ||
          "https://images.unsplash.com/photo-1519225421980-715cb0215aed?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80"
  
        // Get selected services
        const services = []
        document.querySelectorAll('#services-container input[type="checkbox"]:checked').forEach((checkbox) => {
          services.push(checkbox.value)
        })
  
        if (isEditing) {
          // Update existing package
          const packageIndex = appState.packages.findIndex((p) => p.id === Number.parseInt(packageId))
          if (packageIndex !== -1) {
            appState.packages[packageIndex] = {
              ...appState.packages[packageIndex],
              name,
              description,
              price,
              services,
              image,
            }
          }
        } else {
          // Create new package
          const newPackage = {
            id: Date.now(),
            name,
            description,
            price,
            vendorId: appState.currentUser.id,
            vendorName: appState.currentUser.name,
            services,
            image,
          }
  
          appState.packages.push(newPackage)
        }
  
        // Save to localStorage
        localStorage.setItem("weddingPackages", JSON.stringify(appState.packages))
  
        // Return to vendor dashboard
        showSection("vendor-dashboard")
        updateVendorDashboard()
      })
  
      // Cancel package form
      document.getElementById("cancel-package-btn").addEventListener("click", () => {
        showSection("vendor-dashboard")
      })
  
      // Customer dashboard search and filter
      document.getElementById("search-packages").addEventListener("input", () => {
        updateCustomerDashboard()
      })
  
      document.getElementById("filter-services").addEventListener("change", () => {
        updateCustomerDashboard()
      })
  
      // Back to packages button
      document.getElementById("back-to-packages").addEventListener("click", () => {
        showSection("customer-dashboard")
      })
  
      // Cancel booking button
      document.getElementById("cancel-booking-btn").addEventListener("click", () => {
        showSection("package-details-section")
      })
  
      // Booking form
      document.getElementById("booking-form").addEventListener("submit", (e) => {
        e.preventDefault()
  
        const packageId = Number.parseInt(document.getElementById("booking-package-id").value)
        const date = document.getElementById("booking-date").value
        const guests = Number.parseInt(document.getElementById("booking-guests").value)
        const notes = document.getElementById("booking-notes").value
  
        // Create new booking
        const newBooking = {
          id: Date.now(),
          packageId,
          customerId: appState.currentUser.id,
          customerName: appState.currentUser.name,
          date,
          guests,
          notes,
          status: "pending",
          createdAt: new Date().toISOString().split("T")[0],
        }
  
        appState.bookings.push(newBooking)
  
        // Save to localStorage
        localStorage.setItem("weddingBookings", JSON.stringify(appState.bookings))
  
        // Show booking confirmation
        showSection("my-bookings-section")
        updateCustomerBookings()
      })
    }
  
    // Function to show a specific section
    function showSection(sectionId) {
      // Hide all sections
      document.querySelectorAll("main > section").forEach((section) => {
        section.classList.remove("active-section")
      })
  
      // Show the requested section
      document.getElementById(sectionId).classList.add("active-section")
    }
  
    // Function to update the navigation based on user state
    function updateNavigation() {
      const nav = document.getElementById("main-nav")
  
      if (appState.currentUser) {
        // User is logged in
        nav.innerHTML = `
                  <ul>
                      <li><a href="#" id="dashboard-link">Dashboard</a></li>
                      ${appState.userRole === "customer" ? '<li><a href="#" id="my-bookings-link">My Bookings</a></li>' : ""}
                      <li><a href="#" id="logout-link">Logout</a></li>
                  </ul>
              `
  
        // Add event listeners for new nav items
        document.getElementById("dashboard-link").addEventListener("click", (e) => {
          e.preventDefault()
          if (appState.userRole === "vendor") {
            showSection("vendor-dashboard")
            updateVendorDashboard()
          } else {
            showSection("customer-dashboard")
            updateCustomerDashboard()
          }
        })
  
        if (appState.userRole === "customer") {
          document.getElementById("my-bookings-link").addEventListener("click", (e) => {
            e.preventDefault()
            showSection("my-bookings-section")
            updateCustomerBookings()
          })
        }
  
        document.getElementById("logout-link").addEventListener("click", (e) => {
          e.preventDefault()
          appState.currentUser = null
          appState.userRole = null
          updateNavigation()
          showSection("home-section")
        })
      } else {
        // User is not logged in
        nav.innerHTML = `
                  <ul>
                      <li><a href="#" class="active" id="home-link">Home</a></li>
                      <li><a href="#" id="login-link">Login</a></li>
                      <li><a href="#" id="register-link">Register</a></li>
                  </ul>
              `
  
        // Re-add event listeners
        document.getElementById("home-link").addEventListener("click", (e) => {
          e.preventDefault()
          showSection("home-section")
        })
  
        document.getElementById("login-link").addEventListener("click", (e) => {
          e.preventDefault()
          showSection("login-section")
        })
  
        document.getElementById("register-link").addEventListener("click", (e) => {
          e.preventDefault()
          showSection("register-section")
        })
      }
    }
  
    // Function to update the UI
    function updateUI() {
      // Load packages from localStorage
      const storedPackages = localStorage.getItem("weddingPackages")
      if (storedPackages) {
        appState.packages = JSON.parse(storedPackages)
      }
  
      // Load bookings from localStorage
      const storedBookings = localStorage.getItem("weddingBookings")
      if (storedBookings) {
        appState.bookings = JSON.parse(storedBookings)
      }
  
      // Update featured packages on home page
      updateFeaturedPackages()
    }
  
    // Function to update featured packages on home page
    function updateFeaturedPackages() {
      const featuredPackagesContainer = document.getElementById("featured-packages")
  
      // Get up to 3 random packages
      const featuredPackages = appState.packages.sort(() => 0.5 - Math.random()).slice(0, 3)
  
      let packagesHTML = ""
  
      featuredPackages.forEach((pkg) => {
        packagesHTML += `
                  <div class="package-card">
                      <div class="package-image">
                          <img src="${pkg.image}" alt="${pkg.name}">
                      </div>
                      <div class="package-content">
                          <h4>${pkg.name}</h4>
                          <p class="package-vendor">By ${pkg.vendorName}</p>
                          <p class="package-price">$${pkg.price.toLocaleString()}</p>
                          <div class="package-services">
                              ${pkg.services.map((service) => `<span class="service-tag">${service}</span>`).join("")}
                          </div>
                          <div class="package-actions">
                              <button class="btn primary-btn view-package-btn" data-id="${pkg.id}">View Details</button>
                          </div>
                      </div>
                  </div>
              `
      })
  
      featuredPackagesContainer.innerHTML = packagesHTML
  
      // Add event listeners to view package buttons
      document.querySelectorAll(".view-package-btn").forEach((button) => {
        button.addEventListener("click", function () {
          const packageId = Number.parseInt(this.getAttribute("data-id"))
          viewPackageDetails(packageId)
        })
      })
    }
  
    // Function to update vendor dashboard
    function updateVendorDashboard() {
      // Get vendor's packages
      const vendorPackages = appState.packages.filter((pkg) => pkg.vendorId === appState.currentUser.id)
  
      // Get vendor's bookings
      const vendorBookings = appState.bookings.filter((booking) => {
        const pkg = appState.packages.find((p) => p.id === booking.packageId)
        return pkg && pkg.vendorId === appState.currentUser.id
      })
  
      // Update stats
      document.getElementById("total-packages").textContent = vendorPackages.length
      document.getElementById("active-bookings").textContent = vendorBookings.length
  
      // Calculate total revenue (confirmed bookings only)
      const confirmedBookings = vendorBookings.filter((booking) => booking.status === "confirmed")
      let totalRevenue = 0
  
      confirmedBookings.forEach((booking) => {
        const pkg = appState.packages.find((p) => p.id === booking.packageId)
        if (pkg) {
          totalRevenue += pkg.price
        }
      })
  
      document.getElementById("total-revenue").textContent = "$" + totalRevenue.toLocaleString()
  
      // Update packages list
      const vendorPackagesContainer = document.getElementById("vendor-packages")
  
      if (vendorPackages.length === 0) {
        vendorPackagesContainer.innerHTML = `
                  <div class="empty-state">
                      <p>You haven't created any packages yet.</p>
                      <button class="btn primary-btn" id="create-first-package-btn">Create Your First Package</button>
                  </div>
              `
  
        document.getElementById("create-first-package-btn").addEventListener("click", () => {
          document.getElementById("add-package-btn").click()
        })
      } else {
        let packagesHTML = ""
  
        vendorPackages.forEach((pkg) => {
          packagesHTML += `
                      <div class="package-card">
                          <div class="package-image">
                              <img src="${pkg.image}" alt="${pkg.name}">
                          </div>
                          <div class="package-content">
                              <h4>${pkg.name}</h4>
                              <p class="package-price">$${pkg.price.toLocaleString()}</p>
                              <div class="package-services">
                                  ${pkg.services.map((service) => `<span class="service-tag">${service}</span>`).join("")}
                              </div>
                              <div class="package-actions">
                                  <button class="btn secondary-btn edit-package-btn" data-id="${pkg.id}">Edit</button>
                                  <button class="btn primary-btn delete-package-btn" data-id="${pkg.id}">Delete</button>
                              </div>
                          </div>
                      </div>
                  `
        })
  
        vendorPackagesContainer.innerHTML = packagesHTML
  
        // Add event listeners to edit and delete buttons
        document.querySelectorAll(".edit-package-btn").forEach((button) => {
          button.addEventListener("click", function () {
            const packageId = Number.parseInt(this.getAttribute("data-id"))
            editPackage(packageId)
          })
        })
  
        document.querySelectorAll(".delete-package-btn").forEach((button) => {
          button.addEventListener("click", function () {
            const packageId = Number.parseInt(this.getAttribute("data-id"))
            deletePackage(packageId)
          })
        })
      }
    }
  
    // Function to edit a package
    function editPackage(packageId) {
      const pkg = appState.packages.find((p) => p.id === packageId)
  
      if (pkg) {
        // Set form title
        document.getElementById("package-form-title").textContent = "Edit Package"
  
        // Fill form with package data
        document.getElementById("package-id").value = pkg.id
        document.getElementById("package-name").value = pkg.name
        document.getElementById("package-description").value = pkg.description
        document.getElementById("package-price").value = pkg.price
        document.getElementById("package-image").value = pkg.image
  
        // Check service checkboxes
        document.querySelectorAll('#services-container input[type="checkbox"]').forEach((checkbox) => {
          checkbox.checked = pkg.services.includes(checkbox.value)
        })
  
        // Show form section
        showSection("package-form-section")
      }
    }
  
    // Function to delete a package
    function deletePackage(packageId) {
      if (confirm("Are you sure you want to delete this package?")) {
        // Remove package from array
        appState.packages = appState.packages.filter((p) => p.id !== packageId)
  
        // Save to localStorage
        localStorage.setItem("weddingPackages", JSON.stringify(appState.packages))
  
        // Update dashboard
        updateVendorDashboard()
      }
    }
  
    // Function to update customer dashboard
    function updateCustomerDashboard() {
      const searchTerm = document.getElementById("search-packages").value.toLowerCase()
      const serviceFilter = document.getElementById("filter-services").value
  
      // Filter packages based on search and filter
      let filteredPackages = appState.packages
  
      if (searchTerm) {
        filteredPackages = filteredPackages.filter(
          (pkg) =>
            pkg.name.toLowerCase().includes(searchTerm) ||
            pkg.description.toLowerCase().includes(searchTerm) ||
            pkg.vendorName.toLowerCase().includes(searchTerm),
        )
      }
  
      if (serviceFilter) {
        filteredPackages = filteredPackages.filter((pkg) => pkg.services.includes(serviceFilter))
      }
  
      // Update packages grid
      const customerPackagesContainer = document.getElementById("customer-packages")
  
      if (filteredPackages.length === 0) {
        customerPackagesContainer.innerHTML = `
                  <div class="empty-state">
                      <p>No packages found matching your criteria.</p>
                  </div>
              `
      } else {
        let packagesHTML = ""
  
        filteredPackages.forEach((pkg) => {
          packagesHTML += `
                      <div class="package-card">
                          <div class="package-image">
                              <img src="${pkg.image}" alt="${pkg.name}">
                          </div>
                          <div class="package-content">
                              <h4>${pkg.name}</h4>
                              <p class="package-vendor">By ${pkg.vendorName}</p>
                              <p class="package-price">$${pkg.price.toLocaleString()}</p>
                              <div class="package-services">
                                  ${pkg.services.map((service) => `<span class="service-tag">${service}</span>`).join("")}
                              </div>
                              <div class="package-actions">
                                  <button class="btn primary-btn view-package-btn" data-id="${pkg.id}">View Details</button>
                              </div>
                          </div>
                      </div>
                  `
        })
  
        customerPackagesContainer.innerHTML = packagesHTML
  
        // Add event listeners to view package buttons
        document.querySelectorAll(".view-package-btn").forEach((button) => {
          button.addEventListener("click", function () {
            const packageId = Number.parseInt(this.getAttribute("data-id"))
            viewPackageDetails(packageId)
          })
        })
      }
    }
  
    // Function to view package details
    function viewPackageDetails(packageId) {
      const pkg = appState.packages.find((p) => p.id === packageId)
  
      if (pkg) {
        appState.currentPackage = pkg
  
        // Update package details content
        const packageDetailsContent = document.getElementById("package-details-content")
  
        packageDetailsContent.innerHTML = `
                  <div class="package-detail-image">
                      <img src="${pkg.image}" alt="${pkg.name}">
                  </div>
                  <div class="package-detail-content">
                      <div class="package-detail-header">
                          <div class="package-detail-title">
                              <h2>${pkg.name}</h2>
                              <p class="package-detail-vendor">By ${pkg.vendorName}</p>
                          </div>
                          <div class="package-detail-price">$${pkg.price.toLocaleString()}</div>
                      </div>
                      <div class="package-detail-description">
                          <p>${pkg.description}</p>
                      </div>
                      <div class="package-detail-services">
                          <h3>Services Included</h3>
                          <div class="services-list">
                              ${pkg.services.map((service) => `<span class="service-tag">${service}</span>`).join("")}
                          </div>
                      </div>
                      <div class="package-detail-actions">
                          <button class="btn primary-btn" id="book-package-btn">Book This Package</button>
                      </div>
                  </div>
              `
  
        // Show package details section
        showSection("package-details-section")
  
        // Add event listener to book button
        document.getElementById("book-package-btn").addEventListener("click", () => {
          if (!appState.currentUser) {
            // If user is not logged in, redirect to login
            alert("Please login or register to book this package.")
            showSection("login-section")
          } else {
            // Show booking form
            showBookingForm(packageId)
          }
        })
      }
    }
  
    // Function to show booking form
    function showBookingForm(packageId) {
      const pkg = appState.packages.find((p) => p.id === packageId)
  
      if (pkg) {
        // Update booking details
        const bookingDetails = document.getElementById("booking-details")
  
        bookingDetails.innerHTML = `
                  <div class="booking-package">
                      <div class="booking-package-image">
                          <img src="${pkg.image}" alt="${pkg.name}">
                      </div>
                      <div class="booking-package-info">
                          <h3>${pkg.name}</h3>
                          <p class="booking-package-vendor">By ${pkg.vendorName}</p>
                          <p class="booking-package-price">$${pkg.price.toLocaleString()}</p>
                      </div>
                  </div>
              `
  
        // Set package ID in hidden field
        document.getElementById("booking-package-id").value = pkg.id
  
        // Reset form
        document.getElementById("booking-form").reset()
  
        // Set minimum date to tomorrow
        const tomorrow = new Date()
        tomorrow.setDate(tomorrow.getDate() + 1)
        document.getElementById("booking-date").min = tomorrow.toISOString().split("T")[0]
  
        // Show booking section
        showSection("booking-section")
      }
    }
  
    // Function to update customer bookings
    function updateCustomerBookings() {
      // Get customer's bookings
      const customerBookings = appState.bookings.filter((booking) => booking.customerId === appState.currentUser.id)
  
      // Update bookings list
      const customerBookingsContainer = document.getElementById("customer-bookings")
  
      if (customerBookings.length === 0) {
        customerBookingsContainer.innerHTML = `
                  <div class="empty-state">
                      <p>You haven't made any bookings yet.</p>
                      <button class="btn primary-btn" id="browse-packages-btn">Browse Packages</button>
                  </div>
              `
  
        document.getElementById("browse-packages-btn").addEventListener("click", () => {
          showSection("customer-dashboard")
        })
      } else {
        let bookingsHTML = ""
  
        customerBookings.forEach((booking) => {
          const pkg = appState.packages.find((p) => p.id === booking.packageId)
  
          if (pkg) {
            bookingsHTML += `
                          <div class="booking-card">
                              <div class="booking-card-header">
                                  ${pkg.name}
                              </div>
                              <div class="booking-card-content">
                                  <div class="booking-info">
                                      <div class="booking-info-item">
                                          <span class="booking-info-label">Vendor:</span>
                                          <span>${pkg.vendorName}</span>
                                      </div>
                                      <div class="booking-info-item">
                                          <span class="booking-info-label">Date:</span>
                                          <span>${booking.date}</span>
                                      </div>
                                      <div class="booking-info-item">
                                          <span class="booking-info-label">Guests:</span>
                                          <span>${booking.guests}</span>
                                      </div>
                                      <div class="booking-info-item">
                                          <span class="booking-info-label">Price:</span>
                                          <span>$${pkg.price.toLocaleString()}</span>
                                      </div>
                                  </div>
                                  <div class="booking-status ${booking.status === "confirmed" ? "status-confirmed" : "status-pending"}">
                                      ${booking.status.charAt(0).toUpperCase() + booking.status.slice(1)}
                                  </div>
                              </div>
                          </div>
                      `
          }
        })
  
        customerBookingsContainer.innerHTML = bookingsHTML
      }
    }
  })
  
  