const openModal = (t) =>{
    const userId = t.getAttribute("customer-id")
    const userIdInput = $("#userId").val(userId)
}

$("#emailFilter").keyup(function(e){
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
