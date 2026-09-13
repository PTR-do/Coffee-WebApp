//main script: maintenance_home.js

//second script for state update
$(document).ready(function () {
    $updateForm = $('#update-state-form');
    $updateButton = $('#updateButton');

    $updateForm.on('submit', updateState);
});
//---

let $updateForm;
let $updateButton;

//update function to backend
function updateState(event) {
    event.preventDefault();
    const distributorId = $('#panel-machine-id').text().trim();
    const stateValue = $('#dist-status').val();

    // Raccolta Errori
    const errorsValue = {};
    for (let i = 1; i <= 5; i++) {
        errorsValue['e' + i] = $('#e' + i).is(':checked') ? "1" : "0";
    }
    // Raccolta Forniture
    const maxChecked = $('#max-supplies').is(':checked');
    const noChecked = $('#no-supplies').is(':checked');
    const supplyFields = ['bicchierini', 'palettine', 'acqua', 'zucchero', 'caffe', 'latte', 'ginseng', 'cioccolata', 'vaniglia'];
    const maxValues = { 'bicchierini': 1000, 'palettine': 1000, 'acqua': 10, 'zucchero': 5, 'caffe': 10, 'latte': 5, 'ginseng': 1000, 'cioccolata': 1000, 'vaniglia': 1000 };
    const suppliesValue = {};
    let notValid = false;
    supplyFields.forEach(id => {
        if (maxChecked) {
            suppliesValue[id] = maxValues[id];
        } else if (noChecked) {
            suppliesValue[id] = -1; // placeholder
        } else {
            const val = parseFloat($('#input-' + id).val());
            if (isNaN(val) || val < 0) notValid = true;
            suppliesValue[id] = val;
        }
    });
    if (notValid) {
        showHintUpdate("Valore errato nelle forniture");
        return;
    }

    const ns = "http://www.data.com/distributor";
    const xmlDoc = document.implementation.createDocument(ns, "distributorState", null);
    const root = xmlDoc.documentElement;
    const distributor = xmlDoc.createElementNS(ns, "distributor");
    const createNode = (name, text) => {
        const el = xmlDoc.createElementNS(ns, name);
        el.textContent = text;
        return el;
    };
    distributor.appendChild(createNode("id", distributorId));
    distributor.appendChild(createNode("state", stateValue));
    distributor.appendChild(createNode("stateTime", new Date().toISOString().split('.')[0]));
    distributor.appendChild(createNode("updateMaintainer", $('#maintainer-id').text().trim()));
    const errorsNode = xmlDoc.createElementNS(ns, "errors");
    Object.keys(errorsValue).forEach(key => errorsNode.appendChild(createNode(key, errorsValue[key])));
    distributor.appendChild(errorsNode);
    const suppliesNode = xmlDoc.createElementNS(ns, "supplies");
    Object.keys(suppliesValue).forEach(key => suppliesNode.appendChild(createNode(key, suppliesValue[key])));
    distributor.appendChild(suppliesNode);
    root.appendChild(distributor);

    $.ajax({
        url: backendUrl + '/serviceSpringBoot/maintenance/distributorState/updateState',
        method: 'POST',
        contentType: 'application/xml',
        data: new XMLSerializer().serializeToString(xmlDoc),
        success: function(response) {
            if (response && response.result) {
                showHint('Aggiornamento completato!');
                $statePanel.removeClass('visible');
            } else { showHintUpdate(response.message); }
        },
        error: function() {
            showHintUpdate('Errore di comunicazione');
        }
    });
}

//helper
let timerUpdate=null;
function showHintUpdate(text) {
    clearTimeout(timerUpdate);
    $updateButton.text(text);
    timerUpdate = setTimeout(function (){
        $updateButton.text("Aggiorna stato distributore");
    },3000);
}
