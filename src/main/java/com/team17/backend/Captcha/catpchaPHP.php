function isValid() {
    try {
        $url = 'https://www.google.com/recaptcha/api/siteverify';
        $data = [
            'secret'   => '6LclyfEpAAAAAKQfhPDayfV8lVZ7rKjcIwRX2GOR',
            'response' => $_POST['g-recaptcha-response'],
            'remoteip' => $_SERVER['REMOTE_ADDR']
        ];

        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));

        $response = curl_exec($ch);
        
        if ($response === false) {
            throw new Exception('cURL error: ' . curl_error($ch));
        }

        $responseData = json_decode($response, true);

        if (!$responseData || !isset($responseData['success'])) {
            throw new Exception('Invalid response from reCAPTCHA server');
        }

        return $responseData['success'];
    } catch (Exception $e) {
        echo 'Exception: ' . $e->getMessage();
        return null;
    } finally {
        curl_close($ch);
    }
}
