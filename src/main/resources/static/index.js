$(document).ready(function(){
    $("#check-balance-btn").on("click", function(){
        $("#credit-card-div").toggle()
    })

    $(document).on('keypress', function(e){
        const credit_card_div = $("#credit-card-div")
        if(e.which == 13 && credit_card_div.css("display") == "block"){
            const credit_card_number = $("#credit-card-number").val()
            const credit_card_response_succ = $("#credit-card-response-succ")
            const credit_card_response_err = $("#credit-card-response-err")

                $.ajax({
                    method: "POST",
                    url: baseUrl + "api/get_card_balance",
                    data: {
                        "cardNumber" : credit_card_number
                    }
                })
                    .done(function(result){
                        switch(result.apiStatusCode){
                            case 404:
                                credit_card_response_succ.css("display","none")
                                credit_card_response_err.css("display","block")
                                credit_card_response_err.text(result.apiMessage);
                                break;
                            case 200:
                                credit_card_response_err.css("display","none")
                                credit_card_response_succ.css("display","block")
                                credit_card_response_succ.text(result.apiMessage);
                                break;
                            default:
                                break;
                        }
                    })
        }
    });
});
