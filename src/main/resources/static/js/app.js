// const API_BASE_URL = 'http://localhost:8000/api';
//
// function showMessage(elementId, message, isError = true) {
//     const messageElement = document.getElementById(elementId);
//     messageElement.textContent = message;
//     messageElement.classList.toggle('text-red-500', isError);
//     messageElement.classList.toggle('text-green-500', !isError);
//     messageElement.classList.remove('hidden');
// }
//
// function checkAuth() {
//     const token = localStorage.getItem('token');
//     const logoutBtn = document.getElementById('logoutBtn');
//     if (token) {
//         logoutBtn.classList.remove('hidden');
//     } else {
//         logoutBtn.classList.add('hidden');
//     }
// }
//
// function logout() {
//     localStorage.removeItem('token');
//     window.location.href = '/login.html';
// }
//
// document.addEventListener('DOMContentLoaded', () => {
//     checkAuth();
//     const logoutBtn = document.getElementById('logoutBtn');
//     if (logoutBtn) {
//         logoutBtn.addEventListener('click', logout);
//     }
//
//     // Register Form
//     const registerForm = document.getElementById('registerForm');
//     if (registerForm) {
//         registerForm.addEventListener('submit', async (e) => {
//             e.preventDefault();
//             const userData = {
//                 email: document.getElementById('email').value,
//                 password: document.getElementById('password').value,
//                 dob: document.getElementById('dob').value,
//                 profession: document.getElementById('profession').value,
//                 mobile_number: document.getElementById('mobile_number').value
//             };
//             try {
//                 const response = await fetch(`${API_BASE_URL}/users/register`, {
//                     method: 'POST',
//                     headers: { 'Content-Type': 'application/json' },
//                     body: JSON.stringify(userData)
//                 });
//                 if (response.ok) {
//                     showMessage('registerMessage', 'Registration successful! Please login.', false);
//                     setTimeout(() => window.location.href = '/login.html', 2000);
//                 } else {
//                     const error = await response.json();
//                     showMessage('registerMessage', error.message || 'Registration failed');
//                 }
//             } catch (error) {
//                 showMessage('registerMessage', 'Error connecting to server');
//             }
//         });
//     }
//
//     // Login Form
//     const loginForm = document.getElementById('loginForm');
//     if (loginForm) {
//         loginForm.addEventListener('submit', async (e) => {
//             e.preventDefault();
//             const credentials = {
//                 email: document.getElementById('email').value,
//                 password: document.getElementById('password').value
//             };
//             try {
//                 const response = await fetch(`${API_BASE_URL}/users/login`, {
//                     method: 'POST',
//                     headers: { 'Content-Type': 'application/json' },
//                     body: JSON.stringify(credentials)
//                 });
//                 if (response.ok) {
//                     const data = await response.json();
//                     localStorage.setItem('token', data.token);
//                     showMessage('loginMessage', 'Login successful!', false);
//                     setTimeout(() => window.location.href = '/urls.html', 2000);
//                 } else {
//                     const error = await response.json();
//                     showMessage('loginMessage', error.message || 'Login failed');
//                 }
//             } catch (error) {
//                 showMessage('loginMessage', 'Error connecting to server');
//             }
//         });
//     }
//
//     // URL Form
//     const urlForm = document.getElementById('urlForm');
//     if (urlForm) {
//         urlForm.addEventListener('submit', async (e) => {
//             e.preventDefault();
//             const urlData = {
//                 url_name: document.getElementById('url_name').value,
//                 url_category: document.getElementById('url_category').value,
//                 url_description: document.getElementById('url_description').value,
//                 url_link: document.getElementById('url_link').value
//             };
//             try {
//                 const response = await fetch(`${API_BASE_URL}/urls/insert`, {
//                     method: 'POST',
//                     headers: {
//                         'Content-Type': 'application/json',
//                         'token': localStorage.getItem('token')
//                     },
//                     body: JSON.stringify(urlData)
//                 });
//                 if (response.ok) {
//                     showMessage('urlMessage', 'URL added successfully!', false);
//                     urlForm.reset();
//                     loadUrls();
//                 } else {
//                     showMessage('urlMessage', 'Failed to add URL');
//                 }
//             } catch (error) {
//                 showMessage('urlMessage', 'Error connecting to server');
//             }
//         });
//     }
//
//     // Load URLs
//     async function loadUrls() {
//         const urlList = document.getElementById('urlList');
//         if (urlList) {
//             try {
//                 const response = await fetch(`${API_BASE_URL}/urls/retrieve-url-by-user-id`, {
//                     headers: { 'token': localStorage.getItem('token') }
//                 });
//                 if (response.ok) {
//                     const data = await response.json();
//                     urlList.innerHTML = '';
//                     data.urls_list.forEach(url => {
//                         const urlDiv = document.createElement('div');
//                         urlDiv.className = 'bg-white p-4 rounded-lg shadow-md';
//                         urlDiv.innerHTML = `
//                             <p><strong>Name:</strong> ${url.urlName}</p>
//                             <p><strong>Category:</strong> ${url.urlCategory}</p>
//                             <p><strong>Description:</strong> ${url.urlDescription}</p>
//                             <p><strong>Link:</strong> <a href="${url.url_link}" class="text-blue-600 hover:underline" target="_blank">${url.urlLink}</a></p>
//                             <button class="deleteUrlBtn bg-red-600 text-white p-2 rounded hover:bg-red-700 mt-2" data-url-id="${url.urlId}">Delete</button>
//                         `;
//                         urlList.appendChild(urlDiv);
//                     });
//                     document.querySelectorAll('.deleteUrlBtn').forEach(btn => {
//                         btn.addEventListener('click', async () => {
//                             const urlId = btn.getAttribute('data-url-id');
//                             try {
//                                 const response = await fetch(`${API_BASE_URL}/urls/delete-url?url_id=${urlId}`, {
//                                     method: 'DELETE',
//                                     headers: { 'token': localStorage.getItem('token') }
//                                 });
//                                 if (response.ok) {
//                                     showMessage('urlMessage', 'URL deleted successfully!', false);
//                                     loadUrls();
//                                 } else {
//                                     showMessage('urlMessage', 'Failed to delete URL');
//                                 }
//                             } catch (error) {
//                                 showMessage('urlMessage', 'Error connecting to server');
//                             }
//                         });
//                     });
//                 } else {
//                     showMessage('urlMessage', 'Failed to load URLs');
//                 }
//             } catch (error) {
//                 showMessage('urlMessage', 'Error connecting to server');
//             }
//         }
//     }
//
//     // Load Profile
//     async function loadProfile() {
//         const profileDetails = document.getElementById('profileDetails');
//         if (profileDetails) {
//             try {
//                 const response = await fetch(`${API_BASE_URL}/users/retrieve-user`, {
//                     headers: { 'token': localStorage.getItem('token') }
//                 });
//                 if (response.ok) {
//                     const user = await response.json();
//                     profileDetails.innerHTML = `
//                         <p><strong>User ID:</strong> ${user.user_id}</p>
//                         <p><strong>Email:</strong> ${user.email}</p>
//                         <p><strong>Date of Birth:</strong> ${user.dob}</p>
//                         <p><strong>Profession:</strong> ${user.profession}</p>
//                         <p><strong>Mobile Number:</strong> ${user.mobile_number}</p>
//                     `;
//                 } else {
//                     showMessage('profileMessage', 'Failed to load profile');
//                 }
//             } catch (error) {
//                 showMessage('profileMessage', 'Error connecting to server');
//             }
//         }
//     }
//
//     // Delete Account
//     const deleteAccountBtn = document.getElementById('deleteAccountBtn');
//     if (deleteAccountBtn) {
//         deleteAccountBtn.addEventListener('click', async () => {
//             if (confirm('Are you sure you want to delete your account?')) {
//                 try {
//                     const response = await fetch(`${API_BASE_URL}/users/delete`, {
//                         method: 'DELETE',
//                         headers: { 'token': localStorage.getItem('token') }
//                     });
//                     if (response.ok) {
//                         localStorage.removeItem('token');
//                         showMessage('profileMessage', 'Account deleted successfully!', false);
//                         setTimeout(() => window.location.href = '/login.html', 2000);
//                     } else {
//                         showMessage('profileMessage', 'Failed to delete account');
//                     }
//                 } catch (error) {
//                     showMessage('profileMessage', 'Error connecting to server');
//                 }
//             }
//         });
//     }
//
//     // Initialize page-specific functions
//     loadUrls();
//     loadProfile();
// });







//
// // ✅ js/app.js
// const API_BASE_URL = 'http://localhost:8000/api';
//
// function showMessage(elementId, message, isError = true) {
//     const messageElement = document.getElementById(elementId);
//     if (!messageElement) return;
//     messageElement.textContent = message;
//     messageElement.classList.toggle('text-red-500', isError);
//     messageElement.classList.toggle('text-green-500', !isError);
//     messageElement.classList.remove('hidden');
// }
//
// function checkAuth() {
//     const token = localStorage.getItem('token');
//     const logoutBtn = document.getElementById('logoutBtn');
//     if (logoutBtn) {
//         if (token) logoutBtn.classList.remove('hidden');
//         else logoutBtn.classList.add('hidden');
//     }
// }
//
// function logout() {
//     localStorage.removeItem('token');
//     window.location.href = 'login.html';
// }
//
// document.addEventListener('DOMContentLoaded', () => {
//     checkAuth();
//
//     const logoutBtn = document.getElementById('logoutBtn');
//     if (logoutBtn) logoutBtn.addEventListener('click', logout);
//
//     // Register
//     const registerForm = document.getElementById('registerForm');
//     if (registerForm) {
//         registerForm.addEventListener('submit', async (e) => {
//             e.preventDefault();
//             const userData = {
//                 email: document.getElementById('email').value,
//                 password: document.getElementById('password').value,
//                 username: document.getElementById('username').value, // ✅ Add this
//                 dob: document.getElementById('dob').value,
//                 profession: document.getElementById('profession').value,
//                 mobileNumber: document.getElementById('mobile_number').value
//             };
//             try {
//                 const res = await fetch(`${API_BASE_URL}/user/register`, {
//                     method: 'POST',
//                     headers: { 'Content-Type': 'application/json' },
//                     body: JSON.stringify(userData)
//                 });
//                 if (res.ok) {
//                     showMessage('registerMessage', 'Registered successfully!', false);
//                     setTimeout(() => window.location.href = 'login.html', 1500);
//                 } else {
//                     const error = await res.json();
//                     showMessage('registerMessage', error.message || 'Registration failed');
//                 }
//             } catch (err) {
//                 showMessage('registerMessage', 'Network error');
//             }
//         });
//     }
//
//     // Login
//     const loginForm = document.getElementById('loginForm');
//     if (loginForm) {
//         loginForm.addEventListener('submit', async (e) => {
//             e.preventDefault();
//             const creds = {
//                 email: document.getElementById('email').value,
//                 password: document.getElementById('password').value
//             };
//             try {
//                 const res = await fetch(`${API_BASE_URL}/user/login`, {
//                     method: 'POST',
//                     headers: { 'Content-Type': 'application/json' },
//                     body: JSON.stringify(creds)
//                 });
//                 const data = await res.json();
//                 if (data.token) {
//                     localStorage.setItem('token', data.token);
//                     localStorage.setItem('email', data.email);
//                     showMessage('loginMessage', 'Login successful!', false);
//                     setTimeout(() => window.location.href = 'urls.html', 1500);
//                 } else {
//                     showMessage('loginMessage', data.message || 'Login failed');
//                 }
//             } catch (err) {
//                 showMessage('loginMessage', 'Network error');
//             }
//         });
//     }
//
//     // Add URL
//     const urlForm = document.getElementById('urlForm');
//     if (urlForm) {
//         urlForm.addEventListener('submit', async (e) => {
//             e.preventDefault();
//             const payload = {
//                 urlName: document.getElementById('url_name').value,
//                 urlCategory: document.getElementById('url_category').value,
//                 urlDescription: document.getElementById('url_description').value,
//                 urlLink: document.getElementById('url_link').value
//             };
//             try {
//                 const res = await fetch(`${API_BASE_URL}/url/add`, {
//                     method: 'POST',
//                     headers: {
//                         'Content-Type': 'application/json',
//                         'token': localStorage.getItem('token')
//                     },
//                     body: JSON.stringify(payload)
//                 });
//                 if (res.ok) {
//                     showMessage('urlMessage', 'URL added!', false);
//                     urlForm.reset();
//                     loadUrls();
//                 } else {
//                     showMessage('urlMessage', 'Failed to add URL');
//                 }
//             } catch {
//                 showMessage('urlMessage', 'Network error');
//             }
//         });
//     }
//
//     // Load URLs
//     async function loadUrls() {
//         const list = document.getElementById('urlList');
//         if (!list) return;
//         try {
//             const res = await fetch(`${API_BASE_URL}/url/all`, {
//                 headers: { 'token': localStorage.getItem('token') }
//             });
//             const urls = await res.json();
//             list.innerHTML = '';
//             urls.forEach(url => {
//                 const div = document.createElement('div');
//                 div.className = 'bg-white p-4 rounded shadow';
//                 div.innerHTML = `
//                     <p><strong>Name:</strong> ${url.urlName}</p>
//                     <p><strong>Category:</strong> ${url.urlCategory}</p>
//                     <p><strong>Description:</strong> ${url.urlDescription}</p>
//                     <p><strong>Link:</strong> <a href="${url.urlLink}" target="_blank" class="text-blue-600 underline">${url.urlLink}</a></p>
//                     <button class="deleteUrlBtn mt-2 bg-red-600 text-white px-3 py-1 rounded" data-id="${url.urlId}">Delete</button>
//                 `;
//                 list.appendChild(div);
//             });
//             document.querySelectorAll('.deleteUrlBtn').forEach(btn => {
//                 btn.addEventListener('click', async () => {
//                     const id = btn.getAttribute('data-id');
//                     await fetch(`${API_BASE_URL}/url/delete/${id}`, {
//                         method: 'DELETE',
//                         headers: { 'token': localStorage.getItem('token') }
//                     });
//                     loadUrls();
//                 });
//             });
//         } catch {
//             showMessage('urlMessage', 'Could not load URLs');
//         }
//     }
//
//     // Load Profile
//     async function loadProfile() {
//         const profile = document.getElementById('profileDetails');
//         if (!profile) return;
//         try {
//             const res = await fetch(`${API_BASE_URL}/user/retrieve-user`, {
//                 headers: { 'token': localStorage.getItem('token') }
//             });
//             const data = await res.json();
//             profile.innerHTML = `
//                 <p><strong>User ID:</strong> ${data.user_id}</p>
//                 <p><strong>Email:</strong> ${data.email}</p>
//                 <p><strong>Profession:</strong> ${data.profession}</p>
//                 <p><strong>Mobile:</strong> ${data.mobile_number}</p>
//                 <p><strong>DOB:</strong> ${data.dob}</p>
//             `;
//         } catch {
//             showMessage('profileMessage', 'Could not load profile');
//         }
//     }
//
//     const deleteAccountBtn = document.getElementById('deleteAccountBtn');
//     if (deleteAccountBtn) {
//         deleteAccountBtn.addEventListener('click', async () => {
//             if (confirm('Are you sure?')) {
//                 await fetch(`${API_BASE_URL}/user/delete`, {
//                     method: 'DELETE',
//                     headers: { 'token': localStorage.getItem('token') }
//                 });
//                 localStorage.clear();
//                 window.location.href = 'register.html';
//             }
//         });
//     }
//
//     // Auto-load pages
//     loadUrls();
//     loadProfile();
// });








// ✅ Updated app.js with Edit URL and Update Profile support
const API_BASE_URL = 'http://localhost:8000/api';

function showMessage(elementId, message, isError = true) {
    const messageElement = document.getElementById(elementId);
    if (!messageElement) return;
    messageElement.textContent = message;
    messageElement.classList.toggle('text-red-500', isError);
    messageElement.classList.toggle('text-green-500', !isError);
    messageElement.classList.remove('hidden');
}

function checkAuth() {
    const token = localStorage.getItem('token');
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        if (token) logoutBtn.classList.remove('hidden');
        else logoutBtn.classList.add('hidden');
    }
}

function logout() {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const userData = {
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
                username: document.getElementById('username').value,
                dob: document.getElementById('dob').value,
                profession: document.getElementById('profession').value,
                mobileNumber: document.getElementById('mobile_number').value
            };
            try {
                const res = await fetch(`${API_BASE_URL}/user/register`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(userData)
                });
                if (res.ok) {
                    showMessage('registerMessage', 'Registered successfully!', false);
                    setTimeout(() => window.location.href = 'login.html', 1500);
                } else {
                    const error = await res.json();
                    showMessage('registerMessage', error.message || 'Registration failed');
                }
            } catch {
                showMessage('registerMessage', 'Network error');
            }
        });
    }

    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const creds = {
                email: document.getElementById('email').value,
                password: document.getElementById('password').value
            };
            try {
                const res = await fetch(`${API_BASE_URL}/user/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(creds)
                });
                const data = await res.json();
                if (data.token) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('email', data.email);
                    showMessage('loginMessage', 'Login successful!', false);
                    setTimeout(() => window.location.href = 'urls.html', 1500);
                } else {
                    showMessage('loginMessage', data.message || 'Login failed');
                }
            } catch {
                showMessage('loginMessage', 'Network error');
            }
        });
    }

    const urlForm = document.getElementById('urlForm');
    if (urlForm) {
        urlForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const payload = {
                urlName: document.getElementById('url_name').value,
                urlCategory: document.getElementById('url_category').value,
                urlDescription: document.getElementById('url_description').value,
                urlLink: document.getElementById('url_link').value
            };
            try {
                const res = await fetch(`${API_BASE_URL}/url/add`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'token': localStorage.getItem('token')
                    },
                    body: JSON.stringify(payload)
                });
                if (res.ok) {
                    showMessage('urlMessage', 'URL added!', false);
                    urlForm.reset();
                    loadUrls();
                } else {
                    showMessage('urlMessage', 'Failed to add URL');
                }
            } catch {
                showMessage('urlMessage', 'Network error');
            }
        });
    }

    async function loadUrls() {
        const list = document.getElementById('urlList');
        if (!list) return;
        try {
            const res = await fetch(`${API_BASE_URL}/url/all`, {
                headers: { 'token': localStorage.getItem('token') }
            });
            const urls = await res.json();
            list.innerHTML = '';
            urls.forEach(url => {
                const div = document.createElement('div');
                div.className = 'bg-white p-4 rounded shadow';
                div.innerHTML = `
                    <p><strong>Name:</strong> ${url.urlName}</p>
                    <p><strong>Category:</strong> ${url.urlCategory}</p>
                    <p><strong>Description:</strong> ${url.urlDescription}</p>
                    <p><strong>Link:</strong> <a href="${url.urlLink}" target="_blank" class="text-blue-600 underline">${url.urlLink}</a></p>
                    <button class="deleteUrlBtn mt-2 bg-red-600 text-white px-3 py-1 rounded" data-id="${url.urlId}">Delete</button>
                    <button class="editUrlBtn mt-2 ml-2 bg-green-600 text-white px-3 py-1 rounded" 
                        data-id="${url.urlId}"
                        data-name="${url.urlName}"
                        data-category="${url.urlCategory}"
                        data-description="${url.urlDescription}"
                        data-link="${url.urlLink}">Edit</button>
                `;
                list.appendChild(div);
            });

            document.querySelectorAll('.deleteUrlBtn').forEach(btn => {
                btn.addEventListener('click', async () => {
                    const id = btn.getAttribute('data-id');
                    await fetch(`${API_BASE_URL}/url/delete/${id}`, {
                        method: 'DELETE',
                        headers: { 'token': localStorage.getItem('token') }
                    });
                    loadUrls();
                });
            });

            document.querySelectorAll('.editUrlBtn').forEach(btn => {
                btn.addEventListener('click', () => {
                    document.getElementById('edit_url_id').value = btn.dataset.id;
                    document.getElementById('edit_url_name').value = btn.dataset.name;
                    document.getElementById('edit_url_category').value = btn.dataset.category;
                    document.getElementById('edit_url_description').value = btn.dataset.description;
                    document.getElementById('edit_url_link').value = btn.dataset.link;
                    document.getElementById('editModal').classList.remove('hidden');
                });
            });
        } catch {
            showMessage('urlMessage', 'Could not load URLs');
        }
    }

    const editUrlForm = document.getElementById('editUrlForm');
    if (editUrlForm) {
        editUrlForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const id = document.getElementById('edit_url_id').value;
            const payload = {
                urlName: document.getElementById('edit_url_name').value,
                urlCategory: document.getElementById('edit_url_category').value,
                urlDescription: document.getElementById('edit_url_description').value,
                urlLink: document.getElementById('edit_url_link').value
            };
            try {
                await fetch(`${API_BASE_URL}/url/update/${id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'token': localStorage.getItem('token')
                    },
                    body: JSON.stringify(payload)
                });
                document.getElementById('editModal').classList.add('hidden');
                loadUrls();
            } catch {
                alert('Failed to update URL');
            }
        });

        const cancelBtn = document.getElementById('cancelEditBtn');
        if (cancelBtn) {
            cancelBtn.addEventListener('click', () => {
                document.getElementById('editModal').classList.add('hidden');
            });
        }
    }

    async function loadProfile() {
        const profile = document.getElementById('profileDetails');
        if (!profile) return;
        try {
            const res = await fetch(`${API_BASE_URL}/user/retrieve-user`, {
                headers: { 'token': localStorage.getItem('token') }
            });
            const data = await res.json();
            // profile.innerHTML = `
            //     <p><strong>User ID:</strong> ${data.user_id}</p>
            //     <p><strong>Email:</strong> ${data.email}</p>
            //     <p><strong>Profession:</strong> ${data.profession}</p>
            //     <p><strong>Mobile:</strong> ${data.mobile_number}</p>
            //     <p><strong>DOB:</strong> ${data.dob}</p>
            // `;
                profile.innerHTML = `
                  <div class="space-y-10">
                    <div><span class="font-bold">User ID:</span> ${data.user_id}</div>
                    <div><span class="font-bold">Email:</span> ${data.email}</div>
                    <div><span class="font-bold">Profession:</span> ${data.profession}</div>
                    <div><span class="font-bold">Mobile:</span> ${data.mobile_number}</div>
                    <div><span class="font-bold">DOB:</span> ${data.dob}</div>
                  </div>
                `;

//              profile.innerHTML = `
//                <div><span class="font-semibold">User ID: </span>${data.user_id}</div>
//                <div><span class="font-semibold">Email: </span>${data.email}</div>
//                <div><span class="font-semibold">Profession: </span>${data.profession}</div>
//                <div><span class="font-semibold">Mobile: </span>${data.mobile_number}</div>
//                <div><span class="font-semibold">DOB: </span>${data.dob}</div>
//              `;


//            profile.innerHTML = `
//              <div class="flex justify-between py-1"><span class="font-semibold">User ID:</span> <span>${data.user_id}</span></div>
//              <div class="flex justify-between py-1"><span class="font-semibold">Email:</span> <span>${data.email}</span></div>
//              <div class="flex justify-between py-1"><span class="font-semibold">Profession:</span> <span>${data.profession}</span></div>
//              <div class="flex justify-between py-1"><span class="font-semibold">Mobile:</span> <span>${data.mobile_number}</span></div>
//              <div class="flex justify-between py-1"><span class="font-semibold">DOB:</span> <span>${data.dob}</span></div>
//            `;



            // const updateForm = document.getElementById('updateProfileForm');
            // if (updateForm) {
            //     document.getElementById('update_username').value = data.username;
            //     document.getElementById('update_email').value = data.email;
            //     document.getElementById('update_profession').value = data.profession;
            //     document.getElementById('update_mobile').value = data.mobile_number;
            //     document.getElementById('update_dob').value = data.dob;
            // }
        } catch {
            showMessage('profileMessage', 'Could not load profile');
        }
    }

    const updateProfileForm = document.getElementById('updateProfileForm');
    if (updateProfileForm) {
        updateProfileForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const payload = {
                username: document.getElementById('update_username').value,
                email: document.getElementById('update_email').value,
                profession: document.getElementById('update_profession').value,
                mobileNumber: document.getElementById('update_mobile').value,
                dob: document.getElementById('update_dob').value,
                password: 'placeholder' // required to satisfy backend even if not changing password
            };
            try {
                await fetch(`${API_BASE_URL}/user/update`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'token': localStorage.getItem('token')
                    },
                    body: JSON.stringify(payload)
                });
                alert('Profile updated successfully');
                loadProfile();
            } catch {
                alert('Failed to update profile');
            }
        });
    }

    const deleteAccountBtn = document.getElementById('deleteAccountBtn');
    if (deleteAccountBtn) {
        deleteAccountBtn.addEventListener('click', async () => {
            if (confirm('Are you sure?')) {
                await fetch(`${API_BASE_URL}/user/delete`, {
                    method: 'DELETE',
                    headers: { 'token': localStorage.getItem('token') }
                });
                localStorage.clear();
                window.location.href = 'register.html';
            }
        });
    }

    loadUrls();
    loadProfile();
});
