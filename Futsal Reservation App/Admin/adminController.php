<?php
include 'db.php';

header('Content-Type: application/json');

try {
    $stmt = $pdo->prepare("SELECT id, name, phone, email, password, created_at FROM users");
    $stmt->execute();
    
    $users = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    echo json_encode($users);
} catch (Exception $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
?>
