// Start JS
$(document).ready(function(){
    $body = $("body");
    $userCard = $("#user-card");
    $userName = $(".user-name");
    $userCredit = $("#user-credit");
    $userTime = $("#user-time");
    $selectedDrinkField = $("#selected-drink");
    $numBevande = $("#num-bevande");

    setInterval(heartbeat,300000);

    generateMachineCode();

    callBackend();
});
//---

const backendUrl = "http://localhost:9090";
const backendControlUrl = "http://localhost:8080";
let $body;
let $userCard;
let $userName;
let $userCredit;
let $userTime;
let $selectedDrinkField;
let $numBevande;

//control availability
function heartbeat(){
    fetch(backendControlUrl+'/servicejakarta/heartbeat', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            id: $("#id-distributor").text()
        })
    }).then(response => response.json())
    .catch();
}

//random code
function generateMachineCode() {
    function randLetter() {
        return String.fromCharCode(Math.floor(Math.random() * 26) + 65);
    }
    function randDigit() {
        return Math.floor(Math.random() * 10);
    }
    let code = randLetter() + randLetter() + randDigit() + randDigit() + randDigit();
    let $machineCode = $("#machine-code");
    $machineCode.text(code);
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/distributor/machine/codeGeneration',
        method: "POST",
        timeout: 10000,
        contentType: "application/json",
        data: JSON.stringify({
            distributorId: $("#id-distributor").text(),
            distributorCode: code
        }),
        success: function(response) {
            if(!response.result){
                alert(response.message);
                setTimeout(generateMachineCode, 60000);
            }
        },
        error: function () {
            alert("Il server non risponde. Premi ok per riprovare.");
            setTimeout(generateMachineCode, 10000);
        }
    })
}

//start connection time
let remainingSeconds = 60;
let interval = null;
function startCountdown() {
    clearInterval(interval);
    remainingSeconds = 60;
    $userTime.text(formatTime(remainingSeconds));

    interval = setInterval(function () {
        remainingSeconds--;
        if (remainingSeconds <= 0) {
            clearInterval(interval);
            $userCard.css("z-index", 0);
            selectedDrinkName = "";
            selectedDrinkPrice = 0;
            return;
        }
        $userTime.text(formatTime(remainingSeconds));
    }, 1000);

    function formatTime(seconds) {
        let m = Math.floor(seconds / 60);
        let s = seconds % 60;
        return m + ":" + (s < 10 ? "0" + s : s);
    }
}

//poll to backend
let oldCall= null
let userOnline= "";
let serverDown = false;
function callBackend() {
    if(oldCall){
        clearInterval(oldCall);
    }
    let call = function() {
        $.ajax({
            url: backendUrl+'/serviceSpringBoot/distributor/machine/pollCall',
            method: "POST",
            timeout: 2500,
            contentType: "application/json",
            data: JSON.stringify({
                id: $("#id-distributor").text(),
            }),
            success: function(data) {
                serverDown = false;
                let connected = data.connection;
                let userName = data.username || "Utente";
                let userCredit = data.credit || 0.00;
                if (connected) {
                    if(userOnline !== String(userName)){     //controllo di verifica se l'utente è ancora online
                        userOnline = userName;
                        $userName.text(userName);
                        $userCredit.text(userCredit.toFixed(2));
                        startCountdown();

                        connectedUser();   //interazioni con il distributore disponibili solo se user connesso
                    }
                } else if(!connected && userOnline!==""){    //segnalazione disconnessione
                    userOnline= "";
                    $userCard.css("z-index", 0);
                    selectedDrinkName = "";
                    selectedDrinkPrice = 0;
                    generateMachineCode();
                    clearInterval(interval);
                }
            },
            error: function() {
                if(!serverDown) {
                    serverDown = true;
                    $userCard.css("z-index", 0);
                    alert("Impossibile contattare il server. Premi ok per riprovare.");
                }
            }
        });
    };
    oldCall = setInterval(call, 3000);
}

//user selection
let selectedDrinkName = "";
let selectedDrinkPrice = 0;
function connectedUser() {
    $userCard.css("z-index", 2);

    //events
    $(".btn.select").off("click").on("click", function () {        //evento selezione bevanda
        let $article = $(this).closest(".drink-item");
        selectedDrinkName = $article.find("h4").first().text().split("-")[0].trim();
        $selectedDrinkField.text(selectedDrinkName);
        selectedDrinkPrice = parseFloat($article.find("span.price").text().replace("€", "").trim().replace(",", "."));
    });

    $("#confirm-order").off("click").on("click", function () {     //evento conferma ordine
        if (!selectedDrinkName) {
            alert("Seleziona una bevanda prima di confermare.");
            return;
        }
        let numBevande = parseInt($numBevande.val());
        if (isNaN(numBevande) || numBevande < 1 ) numBevande = 1;
        if (numBevande > 5){
            alert("Numero massimo di bevande selezionabili: 5");
            return;
        }
        let totalPrice = selectedDrinkPrice * numBevande;
        let currentCredit = parseFloat($userCredit.text());
        if (currentCredit < totalPrice) {
            alert("Credito insufficiente");
            return;
        }
        let newCredit = currentCredit - totalPrice;
        let sugarLevel = parseInt($("#sugar-level").val()) || 0;
        allowDrink(newCredit, numBevande, sugarLevel);
    });

    $("#cancel-order").off("click").on("click", reset);     //evento annulla

    function showDispense() {
        if ($("#dispense-overlay").length) return;
        let $overlay = $(
            '<div id="dispense-overlay" style="background:rgba(0,0,0,0.8);position:fixed;inset:0;z-index:10;display:flex;align-items:center;justify-content:center;">' +
            '<div style="background:rgba(55,52,52);padding:20px;border-radius:12px;max-width:420px;width:90%;color:#f4e5c8;text-align:center;">' +
            '<div style="font-size:20px;font-weight:700;margin-bottom:12px;">Erogazione in corso</div>' +
            '<div style="background:rgba(255,255,255,0.12);border-radius:999px;height:14px;overflow:hidden;margin-bottom:8px;">' +
            '<div id="dispense-progress-bar" style="height:100%;width:1%;transition:width 8s linear;' +
            'background:linear-gradient(90deg, rgba(255,255,255), rgba(255,255,255));"></div>' +
            '</div>' +
            '</div>' +
            '</div>'
        );
        $body.append($overlay);
        setTimeout(() => {   //attiva la barra di progresso
            $("#dispense-progress-bar").css("width", "100%");
        }, 50);
        setTimeout(() => {   //rimuove overlay erogazione
            $("#dispense-overlay").remove();
            $body.css("overflow", "");
            reset();
        }, 8050);
    }

    function reset(){
        selectedDrinkName = "";
        selectedDrinkPrice = 0;
        $selectedDrinkField.text("—");
        $numBevande.val(1);
        $("#sugar-level").val(0);
    }

    function allowDrink(newCredit, numBevande, sugarLevel){
        $.ajax({
            url: backendUrl + "/serviceSpringBoot/distributor/machine/permission",
            method: "POST",
            timeout: 3000,
            contentType: "application/json",
            data: JSON.stringify({
                id: $("#id-distributor").text(),
                drink: selectedDrinkName,
                drinkNumber: numBevande,
                sugar: sugarLevel
            }),
            success: function(response){
                if (response && response.result) {
                    $userCredit.text(newCredit.toFixed(2));
                    let $creditOverlay = $(
                        '<div id="credit-overlay" style="background:rgba(0,0,0,0.4);position:fixed;inset:0;z-index:20;display:flex;align-items:center;justify-content:center;">' +
                        '<div style="background:rgba(55,52,52);padding:20px;border-radius:12px;max-width:300px;width:90%;color:#f4e5c8;text-align:center;">' +
                        '<div style="font-size:18px;font-weight:700;margin-bottom:8px;">Credito aggiornato</div>' +
                        '<div style="font-size:16px;">Nuovo credito: €'+newCredit.toFixed(2)+'</div>' +
                        '</div>' +
                        '</div>'
                    );
                    $body.css("overflow", "hidden");
                    $body.append($creditOverlay);
                    setTimeout(function () {
                        $creditOverlay.remove();
                        showDispense();
                    }, 3000);
                } else {
                    alert(response.message);
                    reset()
                }
            },
            error: function(){
                alert("Errore di comunicazione, riprovare.");
                reset();
            }
        });
    }
}


