//Start JS
$(document).ready(function () {
    const $creditBtn = $("#credit-btn");
    const $connectBtn = $("#connect-btn");
    const $disconnectBtn = $("#disconnect-btn");
    $usernameField = $("#username-field");
    $credit = $("#credit");
    $state = $("#state");
    username = $usernameField.length ? $usernameField.text() : "";
    credit = $credit.text() || "€0,00";

    //events
    $connectBtn.on("click", requestConnection);          //bottone connessione
    $disconnectBtn.on("click", handleDisconnection);     //bottone disconnessione
    $creditBtn.on("click", RechargeSelection);           //bottone ricarica credito
});
//---

const backendUrl = "http://localhost:9090";
let $usernameField;
let $credit;
let $state;
let username;
let credit;
let distributorId = null;
let timer=null;

//helper
function showSuccess() {
    const $overlaySuccess = $("#overlay-success");
    $overlaySuccess.removeClass("hidden");
    setTimeout(() => $overlaySuccess.addClass("hidden"), 3000);
}
function showErrorOverlay() {
    const $overlayError = $("#overlay-error");
    $overlayError.removeClass("hidden");
    setTimeout(() => $overlayError.addClass("hidden"), 3000);
}

//connection to distributor
function requestConnection() {
    distributorId = $("#distributor-id").val();
    const code = $("#connection-code").val();
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/consumer/connect',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            username: username,
            distributorId: distributorId,
            distributorCode: code
        }),
        success: function (response) {
            if (response && response.result) {
                $state.text(`Connesso: ID ${distributorId}`);
                $state.css("background-color", "blue");
                connectTimeout();
            } else {
                alert(response.message);
            }
        },
        error: function () {
            showErrorOverlay();
        }
    });

    function connectTimeout(){
        timer = setTimeout(()=>{
            $("#state").text("Al momento non connesso");
            $state.css("background-color", "#a58e76");
            distributorId = null;
        },60000);
    }
}

//disconnection
function handleDisconnection() {
    if(distributorId==null) {
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/consumer/disconnect',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            username: username,
            distributorId: distributorId,
        }),
        success: function(response) {
            if (response && response.result) {
                clearTimeout(timer);
                $("#state").text("Al momento non connesso");
                $state.css("background-color", "#a58e76");
                distributorId = null;
            } else {
                alert(response.message);
            }
        },
        error: function() {
            showErrorOverlay();
        }
    });
}

//recharge section
function RechargeSelection() {
    const $overlayRecharge = $("#overlay-recharge");
    const $confirmRecharge = $("#confirm-recharge");
    const $cancelRecharge = $("#cancel-recharge");
    const $amountInputs = $("input[name='recharge-amount']");
    let selectedAmount = 0;
    $amountInputs.prop("checked", false);
    $overlayRecharge.removeClass("hidden");

    //events
    $amountInputs.off("click").on("change", function () {    //selezione importo ricarica
        selectedAmount = parseFloat($(this).val());
    });
    $cancelRecharge.off("click").on("click", function () {   // bottone cancella ricarica
        $overlayRecharge.addClass("hidden");
        selectedAmount = 0;
    });
    $confirmRecharge.off("click").on("click", function () {  // bottone conferma ricarica
        if (selectedAmount > 0) {
            updateCredit(selectedAmount);
            selectedAmount = 0;
        }
    });

    function updateCredit(amount) {
        let numeric = parseFloat(credit.replace("€", "").replace(",", ".").trim());
        numeric += Number(amount);
        credit = numeric.toFixed(2).replace(".", ",");
        $credit.text(`€${credit}`);
        $.ajax({
            url: backendUrl + '/serviceSpringBoot/consumer/recharge',
            method: "POST",
            timeout: 5000,
            contentType: "application/json",
            data: JSON.stringify({
                username: username,
                credit: Number(amount)
            }),
            success: function (response) {
                if (response && response.result) {
                    $overlayRecharge.addClass("hidden");
                    showSuccess();
                } else {
                    alert(response.message);
                }
            },
            error: function () {
                showErrorOverlay();
            }
        })
    }
}