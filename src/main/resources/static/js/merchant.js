//
// const createNewMerchant = async (form) => {
//
//     $.ajax({
//         method: "POST",
//         url: baseUrl + "api/merchant/new",
//         data: {
//             name: form.name,
//             surname: form.surname,
//             email: form.email,
//             password: form.password,
//             token: form.token
//         }
//     })
//         .done(function(result){
//             const errorDiv = $("#merchant-new-error");
//             const successDiv = $("#merchant-new-success");
//             const form = $("#merchant-new-form")
//             switch(result.apiStatusCode){
//                 case 200:
//                     successDiv.text(result.apiMessage)
//                     successDiv.css("display","block")
//                     errorDiv.css("display","none")
//                     form.css("display","none")
//
//                     window.setTimeout(function(){
//                         location.href = baseUrl + "dashboard"
//                     }, 2000)
//                     break;
//                 case 401:
//                     location.href = baseUrl + "login";
//                     break;
//                 case 500:
//                     errorDiv.text(result.apiMessage)
//                     errorDiv.css("display","block")
//                     successDiv.css("display","none")
//                     break;
//                 default:
//                     console.log(result)
//                     break;
//             }
//         })
// }
//
// const createMerchantCard = (merchantData) => {
//     const cardDiv = $("<div></div>")
//     cardDiv.addClass("card")
//     cardDiv.addClass("mx-2")
//     cardDiv.addClass("my-1")
//
//     const cardHeader = $("<div></div>")
//     cardHeader.addClass("card-header")
//
//     const cardBody = $("<div></div>")
//     cardBody.addClass("card-body")
//
//     const cardTitle = $("<h5></h5>").text(merchantData.name)
//     cardTitle.addClass("card-title")
//
//     const cardText = $("<p></p>").text("" + merchantData.email)
//     cardTitle.addClass("card-text")
//
//     cardDiv.append(cardHeader)
//     cardDiv.append(cardBody)
//     cardBody.append(cardTitle)
//     cardBody.append(cardText)
//
//     return cardDiv
// }
//
// const getMerchantsList = async (listContainer) => {
//     const token = getCookie("token")
//
//     $.ajax({
//         method: "POST",
//         url: baseUrl + "api/merchant/list",
//         data: {
//             token: token
//         }
//     })
//         .done(function(result){
//             switch(result.apiStatusCode){
//                 case 200:
//                     const merchantsList = result.apiMessage.map(m => {
//                         return createMerchantCard(m);
//                     })
//                     listContainer.append(merchantsList)
//                     break;
//                 case 401:
//                     location.href = baseUrl + "login";
//                     break;
//                 case 500:
//                     console.log('500 error', result)
//                     break;
//                 default:
//                     console.log(result)
//                     break;
//             }
//         })
// }
//
// $(document).ready(async function() {
//     const merchantNewForm = $("#merchant-new-form")
//     const merchantsListContainer = $("#merchant-list-container")
//
//     if(merchantNewForm){
//         const token = getCookie("token")
//         if(token){
//             const csrf = $("<input></input>")
//
//             csrf.attr("value", token)
//             csrf.attr("name", "token")
//             csrf.attr("id", "token")
//             csrf.attr("type", "hidden")
//
//             merchantNewForm.append(csrf)
//         }
//
//         merchantNewForm.on("submit",function(f){
//             f.preventDefault()
//             const name = $("#name").val()
//             const surname = $("#surname").val()
//             const email = $("#email").val()
//             const password = $("#password").val()
//             const token = $("#token").val()
//             createNewMerchant({
//                 name: name,
//                 surname: surname,
//                 email: email,
//                 password: password,
//                 token: token
//             })
//         })
//     }
//
//     if(merchantsListContainer){
//         getMerchantsList(merchantsListContainer)
//     }
// })