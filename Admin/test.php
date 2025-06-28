// fetch_cancelled_bookings.php
<?php
$firebaseUrl = "https://futsal-reservation-app-default-rtdb.firebaseio.com/bookings.json"; // URL untuk mengambil atau mengirim data ke node "bookings"
$response = file_get_contents($firebaseUrl);
$bookings = json_decode($response, true);

// Filter pesanan dengan status cancelled
$cancelledBookings = [];
foreach ($bookings as $bookingId => $bookingData) {
    if ($bookingData['status'] == 'cancelled') {
        $cancelledBookings[$bookingId] = $bookingData;
    }
}

foreach ($cancelledBookings as $bookingId => $bookingData) {
    echo "<tr>";
    echo "<td>" . $bookingData['atasNama'] . "</td>";
    echo "<td>" . $bookingData['tanggal'] . "</td>";
    echo "<td>" . $bookingData['reason'] . "</td>";
    echo "<td><a href='delete_booking.php?id=$bookingId'>Hapus</a></td>";
    echo "</tr>";
}
?>
