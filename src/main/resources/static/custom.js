$(document).ready(function(){
    $("#check-balance-btn").on("click", function(){
        $("#check-balance-div").css("display","block");
    })

    $(document).on('keypress', function(e){
        if(e.which == 13){
            const card_number = $("#card-number").val()
            const credit_card_response = $("#credit-card-response")

            if(credit_card_response.removeClass("alert alert-primary alert-danger"))

            $.ajax({
                method: "POST",
                url: "get_card_balance",
                data: {
                    "CardNumber" : card_number
                }
            })
                .done(function(result){
                    if(result.balance < 0){
                        credit_card_response.css("display","block");
                        credit_card_response.text("Credit card not found!");
                        credit_card_response.addClass("alert alert-danger");
                    }
                    else{
                        credit_card_response.css("display","block");
                        credit_card_response.text("Card's balance is: " + result.balance + " €");
                        credit_card_response.addClass("alert alert-primary");
                    }
                })
        }
    });
});
