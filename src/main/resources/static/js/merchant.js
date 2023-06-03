
const createNewMerchant = async (form) => {

    console.log(form)

    $.ajax({
        method: "POST",
        url: baseUrl + "api/merchant/new",
        data: {
            name: form.name,
            surname: form.surname,
            email: form.email,
            password: form.password,
            token: form.token
        }
    })
        .done(function(result){
            console.log(result)
            switch(result.apiStatusCode){
                case 200:
                    console.log('ok', result)
                    break;
                case 401:
                    location.href = baseUrl + "login";
                    break;
                default:
                    console.log(result)
                    break;
            }
        })
}

$(document).ready(async function() {
    const merchantNewForm = $("#merchant-new-form")

    if(merchantNewForm){
        const token = getCookie("token")
        if(token){
            const csrf = $("<input></input>")

            csrf.attr("value", token)
            csrf.attr("name", "token")
            csrf.attr("id", "token")
            csrf.attr("type", "hidden")

            merchantNewForm.append(csrf)
        }

        merchantNewForm.on("submit",function(f){
            f.preventDefault()
            const name = $("#name").val()
            const surname = $("#surname").val()
            const email = $("#email").val()
            const password = $("#password").val()
            const token = $("#token").val()
            createNewMerchant({
                name: name,
                surname: surname,
                email: email,
                password: password,
                token: token
            })
        })
    }
})