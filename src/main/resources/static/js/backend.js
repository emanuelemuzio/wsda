if(page){
    $("#" + page + "-li").addClass("active")
}

const openModal = (t) =>{
    const userId = t.getAttribute("customer-id")
    $("#userId").val(userId)
}

const setModal = (modal, t) =>{
    const creditCardId = t.getAttribute("credit-card-id")
    const actualBalance = t.getAttribute("credit-card-balance")
    $("#" + modal + "CreditCardId").val(creditCardId)
    $("#" + modal + "Balance").text(actualBalance)
    $("." + modal + "-max").attr("max",actualBalance)
}

$("#creditCardFilter").keyup(function(e){
    const filter = e.target.value
    const rows = $(".owner-email, .credit-card-number").closest(".credit-card-row")

    for(row of rows){
        if(row.innerText.includes(filter) || row.innerText.includes(filter)){
            row.closest(".credit-card-row").style.display = "table-row"
        }
        else{
            row.closest(".credit-card-row").style.display = "none"
        }
    }
})

$("#emailFilter").keyup(function(e){
    console.log('hi')

    const emailFilter = e.target.value
    const emails = $(".customer-row > .customer-email")

    for(email of emails){
        if(email.innerText.includes(emailFilter)){
            email.closest(".customer-row").style.display = "table-row"
        }
        else{
            email.closest(".customer-row").style.display = "none"
        }
    }
})
