<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Page</title>
  <style>
    body {
      font-family: 'Roboto', sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f4f6f9;
      color: #333;
    }

    header {
      background-color: #007bff;
      color: #fff;
      padding: 1rem 0;
      text-align: center;
    }

    header h1 {
      margin: 0;
      font-size: 2rem;
    }

    .container {
      max-width: 1200px;
      margin: 2rem auto;
      padding: 1rem;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 1.5rem;
    }

    table th, table td {
      text-align: left;
      padding: 0.75rem;
      border: 1px solid #ddd;
    }

    table th {
      background-color: #007bff;
      color: #fff;
    }

    table tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    .btn {
      padding: 0.5rem 1rem;
      border: none;
      border-radius: 4px;
      color: #fff;
      cursor: pointer;
    }

    .btn.edit {
      background-color: #28a745;
    }

    .btn.delete {
      background-color: #dc3545;
    }

    .btn.status {
      background-color: #ffc107;
    }

    form {
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
      margin-top: 2rem;
    }

    input[type="text"], input[type="date"], input[type="time"], input[type="number"] {
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 4px;
    }

    button {
      background-color: #007bff;
      color: #fff;
      border: none;
      border-radius: 4px;
      padding: 0.75rem;
      cursor: pointer;
    }

    button:hover {
      background-color: #0056b3;
    }

    #addOrderForm {
      display: none;
      padding: 1rem;
      background-color: #f9f9f9;
      border: 1px solid #ddd;
      border-radius: 8px;
    }

    .toggle-form-btn {
      margin: 1rem 0;
      background-color: #007bff;
      color: #fff;
      border: none;
      padding: 0.75rem 1.5rem;
      cursor: pointer;
    }

    .toggle-form-btn:hover {
      background-color: #0056b3;
    }
  </style>
  <!-- Impor Firebase SDK -->
  <script type="module">
    import { initializeApp } from 'https://www.gstatic.com/firebasejs/11.1.0/firebase-app.js';
    import { getDatabase, ref, set, remove, update, get } from 'https://www.gstatic.com/firebasejs/11.1.0/firebase-database.js';

    // Konfigurasi Firebase
    const firebaseConfig = {
      apiKey: "AIzaSyCuA1qidu4PAIWKH_ims3T7GYvisc_ZqU8",
      authDomain: "futsal-reservation-app.firebaseapp.com",
      databaseURL: "https://futsal-reservation-app-default-rtdb.firebaseio.com",
      projectId: "futsal-reservation-app",
      storageBucket: "futsal-reservation-app.firebasestorage.app",
      messagingSenderId: "555512018764",
      appId: "1:555512018764:web:956279dd0cc9cccb2141e9",
    };

    // Inisialisasi aplikasi Firebase
    const app = initializeApp(firebaseConfig);
    const db = getDatabase(app);

    // Fungsi untuk menambahkan pesanan ke Firebase
    document.getElementById('addOrderForm').addEventListener('submit', function (event) {
      event.preventDefault(); // Mencegah reload halaman saat submit

      // Ambil nilai dari input form
      const atasNama = document.getElementById('atasNama').value;
      const tanggal = document.getElementById('tanggal').value;
      const waktu = document.getElementById('waktu').value;
      const durasi = document.getElementById('durasi').value;
      const arena = document.getElementById('arena').value;

      // Buat ID unik untuk setiap booking
      const bookingId = 'booking_' + new Date().getTime();

      // Set data di Firebase
      set(ref(db, 'bookings/' + bookingId), {
        atasNama,
        tanggal,
        waktu,
        durasi,
        arena,
        status: "pending"
      }).then(() => {
        alert('Pesanan berhasil ditambahkan!');
        document.getElementById('addOrderForm').reset();
        document.getElementById('addOrderForm').style.display = 'none';
        fetchData(); // Menarik ulang data untuk memperbarui tabel
      }).catch((error) => {
        console.error('Error adding data: ', error);
      });
    });

    // Fungsi untuk mengambil data dari Firebase dan menampilkan di tabel
    // Fungsi untuk mengambil data dari Firebase dan menampilkan di tabel
    function fetchData() {
      // Ambil data booking dari setiap pengguna
      const usersRef = ref(db, 'users');
      get(usersRef).then((snapshot) => {
        if (snapshot.exists()) {
          const users = snapshot.val();
          let html = "";
          // Loop melalui setiap pengguna dan ambil data booking mereka
          for (const userId in users) {
            const bookings = users[userId].bookings;
            for (const bookingId in bookings) {
              const booking = bookings[bookingId];
              html += `
                <tr>
                  <td>${bookingId}</td>
                  <td>${booking.atasNama}</td>
                  <td>${booking.tanggal}</td>
                  <td>${booking.waktu}</td>
                  <td>${booking.durasi}</td>
                  <td>${booking.arena}</td>
                  <td>${booking.status || "pending"}</td>
                  <td>
                    <!-- Tombol Edit dengan Icon -->
                    <button class="edit-btn" data-id="${bookingId}">
                      <i class="fa fa-edit"></i> Edit
                    </button>
                    <!-- Tombol Hapus dengan Icon -->
                    <button class="delete-btn" data-id="${bookingId}">
                      <i class="fa fa-trash"></i> Hapus
                    </button>
                    <button class="status-btn" data-id="${bookingId}" data-status="${booking.status}">
                      ${booking.status || "Pending"}
                    </button>
                  </td>
                </tr>
              `;
            }
          }
          document.getElementById("bookings").innerHTML = html;
        } else {
          document.getElementById("bookings").innerHTML = "<tr><td colspan='7'>No bookings found</td></tr>";
        }
      }).catch((error) => {
        console.error('Error fetching data:', error);
      });
    }
    // Fungsi untuk menandai pesanan sebagai selesai
function markAsCompleted(bookingId) {
  if (confirm("Apakah Anda yakin ingin menandai pesanan ini sebagai selesai?")) {
    update(ref(db, 'bookings/' + bookingId), {
      status: "completed" // Menandai pesanan sebagai selesai
    })
      .then(() => {
        alert("Pesanan berhasil ditandai sebagai selesai!");
        fetchData(); // Refresh data di tabel
      })
      .catch((error) => {
        console.error("Error updating status:", error);
      });
  }
}

// Event delegation untuk menangani klik tombol status (Pending/Selesai)
document.getElementById('bookings').addEventListener('click', function (event) {
  if (event.target.classList.contains('status-btn')) {
    const bookingId = event.target.getAttribute('data-id');
    const currentStatus = event.target.getAttribute('data-status');

    // Toggle status antara "Pending" dan "Completed"
    const newStatus = currentStatus === "Pending" ? "Completed" : "Pending";

    // Update status di Firebase
    update(ref(db, 'bookings/' + bookingId), { status: newStatus })
      .then(() => {
        alert(`Status updated to ${newStatus}`);
        fetchData(); // Refresh data setelah update status
      })
      .catch((error) => {
        console.error('Error updating status:', error);
      });
  }
});


// Fungsi untuk menghapus pesanan
function deleteBooking(bookingId) {
  if (confirm("Are you sure you want to delete this booking?")) {
    // Hapus data pada path yang benar
    const reference = ref(db, 'bookings/' + bookingId);
    remove(reference)
      .then(() => {
        alert('Booking successfully deleted');
        fetchData(); // Refresh data
      })
      .catch((error) => {
        console.error('Error deleting booking:', error);
      });
  }
}

// Event delegation untuk menangani klik tombol edit dan delete
document.getElementById('bookings').addEventListener('click', function (event) {
  if (event.target.classList.contains('delete-btn')) {
    const bookingId = event.target.getAttribute('data-id');
    deleteBooking(bookingId);
  } else if (event.target.classList.contains('edit-btn')) {
    const bookingId = event.target.getAttribute('data-id');
    editBooking(bookingId);
  }
});


    // Fungsi untuk mengedit pesanan
function editBooking(bookingId) {
  console.log("Editing booking ID:", bookingId); // Menambahkan log untuk mengecek ID yang diterima
  // Ambil data booking untuk edit
  const reference = ref(db, 'bookings/' + bookingId);
  get(reference).then((snapshot) => {
    if (snapshot.exists()) {
      const booking = snapshot.val();
      console.log("Booking data:", booking); // Menambahkan log untuk mengecek data booking

      // Isi form dengan data yang akan diedit
      document.getElementById('atasNama').value = booking.atasNama;
      document.getElementById('tanggal').value = booking.tanggal;
      document.getElementById('waktu').value = booking.waktu;
      document.getElementById('durasi').value = booking.durasi;
      document.getElementById('arena').value = booking.arena;

      // Tampilkan form untuk edit
      document.getElementById('addOrderForm').style.display = 'block';
      document.getElementById('showFormBtn').style.display = 'none'; // Sembunyikan tombol tambah

      // Update form untuk pengeditan
      document.getElementById('addOrderForm').onsubmit = function (event) {
        event.preventDefault(); // Mencegah reload halaman saat submit

        // Ambil nilai baru dari input form
        const updatedAtasNama = document.getElementById('atasNama').value;
        const updatedTanggal = document.getElementById('tanggal').value;
        const updatedWaktu = document.getElementById('waktu').value;
        const updatedDurasi = document.getElementById('durasi').value;
        const updatedArena = document.getElementById('arena').value;

        // Update data di Firebase
        update(ref(db, 'bookings/' + bookingId), {
          atasNama: updatedAtasNama,
          tanggal: updatedTanggal,
          waktu: updatedWaktu,
          durasi: updatedDurasi,
          arena: updatedArena
        }).then(() => {
          alert('Booking updated successfully!');
          // Reset form dan sembunyikan
          document.getElementById('addOrderForm').reset();
          document.getElementById('addOrderForm').style.display = 'none';
          fetchData(); // Refresh data
        }).catch((error) => {
          console.error('Error updating booking:', error);
        });
      };
    } else {
      console.log('No such booking!');
    }
  }).catch((error) => {
    console.error('Error fetching data:', error);
  });
}
document.getElementById('addOrderForm').addEventListener('submit', function (event) {
  event.preventDefault();
  const atasNama = document.getElementById('atasNama').value;
  const tanggal = document.getElementById('tanggal').value;
  const waktu = document.getElementById('waktu').value;
  const durasi = document.getElementById('durasi').value;
  const arena = document.getElementById('arena').value;
  const bookingId = 'booking_' + new Date().getTime();

  set(ref(db, 'bookings/' + bookingId), {
    atasNama,
    tanggal,
    waktu,
    durasi,
    arena,
    status: "pending"
  }).then(() => {
    alert('Pesanan berhasil ditambahkan!');
    document.getElementById('addOrderForm').reset();
    fetchData(); // Refresh data
  }).catch((error) => {
    console.error(error);
  });
});

document.getElementById('bookings').addEventListener('click', (event) => {
  if (event.target.classList.contains('delete-btn')) {
    const id = event.target.getAttribute('data-id');
    remove(ref(db, 'bookings/' + id))
      .then(() => {
        alert('Pesanan berhasil dihapus!');
        fetchData(); // Refresh data
      })
      .catch((error) => {
        console.error(error);
      });
  }
});


    // Fungsi untuk menampilkan form
    function showForm() {
      document.getElementById('addOrderForm').style.display = 'block';
      document.getElementById('showFormBtn').style.display = 'none'; // Sembunyikan tombol tambah
    }

    // Ambil data saat halaman dimuat
    window.onload = fetchData;

    // Event delegation untuk menangani klik tombol edit dan delete
    document.getElementById('bookings').addEventListener('click', function (event) {
      if (event.target.classList.contains('delete-btn')) {
        const bookingId = event.target.getAttribute('data-id');
        deleteBooking(bookingId);
      } else if (event.target.classList.contains('edit-btn')) {
        const bookingId = event.target.getAttribute('data-id');
        editBooking(bookingId);
      }
    });
  </script>

  <!-- Impor Font Awesome untuk ikon -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
  <header>
    <h1>Data Penyewaan</h1>
  </header>

  <div class="container">
    <button class="toggle-form-btn" onclick="showForm()">Tambah Pesanan</button>
    <div id="addOrderForm">
      <form>
        <label for="atasNama">Atas Nama:</label>
        <input type="text" id="atasNama" required>
        <label for="tanggal">Tanggal:</label>
        <input type="date" id="tanggal" required>
        <label for="waktu">Waktu:</label>
        <input type="time" id="waktu" required>
        <label for="durasi">Durasi:</label>
        <input type="number" id="durasi" required>
        <label for="arena">Arena:</label>
        <input type="text" id="arena" required>
        <button type="submit">Simpan Pesanan</button>
      </form>
    </div>

    <table id="ordersTable">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nama</th>
          <th>Tanggal</th>
          <th>Waktu</th>
          <th>Durasi</th>
          <th>Arena</th>
          <th>Status</th>
          <th>Aksi</th>
        </tr>
      </thead>
      <tbody id="bookings">
        <!-- Data akan diisi oleh JavaScript -->
      </tbody>
    </table>
  </div>
</body>
</html>
