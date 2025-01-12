$(document).ready(function () {
    $("#payButton").click(function () {
        const requestData = {
            partner_order_id: "12345",
            partner_user_id: "user123",
            item_name: "테스트 상품",
            quantity: 1,
            total_amount: 100,
            tax_free_amount: 0,
            approval_url: "http://localhost:8080/kakaopay/success",
            cancel_url: "http://localhost:8080/kakaopay/cancel",
            fail_url: "http://localhost:8080/kakaopay/fail"
        };

        $.ajax({
            url: "/kakaopay/ready",
            method: "POST",
            data: JSON.stringify(requestData),
            contentType: "application/json",
            success: function (response) {
                // PC 결제 URL로 리다이렉션
                window.location.href = response.next_redirect_pc_url;
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            }
        });
    });
});
