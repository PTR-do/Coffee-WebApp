//Start JS
$(document).ready(function () {
    $fetchBtn = $('#fetch-btn');
    $statePanel = $('#state-panel');
    const $machineIdInput = $('#machine-id');

    $fetchBtn.on('click', function () {
        let id = $machineIdInput.val().trim();
        if (!id) {
            showHint('Inserisci l\'ID distributore per procedere.');
            return;
        }
        DistributorStateRequest(id);
    });
});
//---

const backendUrl = "http://localhost:9090";    //file di esempio: '../../ExampleXml/example.xml'
let $fetchBtn;
let $statePanel;
let xmlResponse = null;

//text under input bar
let oldTimeout;
function showHint(text) {
    const $hint = $('.hint');
    $hint.text(text);
    if(text === 'Caricamento...'){
        $hint.css('color', '#373434');
    } else {
        $hint.css('color', '#b80000');
        if(text === 'Completato'){
            $hint.css('color', '#a58e76');
        }
        clearInterval(oldTimeout);
        oldTimeout = setTimeout(() => {
            $hint.text('Inserisci l\'ID e premi "Carica dati" per ricevere lo stato aggiornato del distributore.');
            $hint.css('color', '#666');
        }, 3000);
    }
}

//request backend
function DistributorStateRequest(machineId) {
    $fetchBtn.prop('disabled', true);
    showHint('Caricamento...');
    if(xmlResponse == null) {
        $.ajax({
            url: backendUrl + '/serviceSpringBoot/maintenance/distributorState/requestState',
            method: 'POST',
            timeout: 8000,
            contentType: 'application/json',
            data: JSON.stringify({
                id: machineId
            }),
            success: function (response) {
                let xml = ParseXml(response);
                if (response && xml) {
                    ParseData(xml, machineId);
                } else {
                    showHint('Errore');
                    $statePanel.removeClass('visible');
                }
                $fetchBtn.prop('disabled', false);
            },
            error: function (jqXHR, textStatus) {
                if (textStatus === 'timeout') {
                    showHint('Timeout: il server non ha risposto in tempo.');
                } else {
                    showHint('Errore');
                }
                $statePanel.removeClass('visible');
                $fetchBtn.prop('disabled', false);
            },
        });
    } else {
        ParseData(xmlResponse, machineId);
    }

    function ParseXml(response) {
        if (!response) return null;
        if (response instanceof XMLDocument) return response;
        try {
            return (new window.DOMParser()).parseFromString(response, "application/xml");
        } catch(e) {
            return null;
        }
    }

}

//xml data
function ParseData(xml, machineId) {
    const $panelMachineId = $('#panel-machine-id');
    const $panelState =$('#panel-state');
    const $panelUpdated = $('#panel-updated');
    const $panelMaintainer = $('#panel-maintainer');
    const $errorsList = $('#errors-list');
    let selected = null;
    const distributors = xml.getElementsByTagName('distributor');
    if (distributors.length === 0) {
        showHint('Nessun risultato trovato.');
        $statePanel.removeClass('visible');
        return;
    } else {
        xmlResponse = xml;
    }
    for (let d of distributors) {
        let idNode = d.getElementsByTagName('id')[0];
        if (idNode && idNode.textContent === machineId) {
            selected = d;
            break;
        }
    }
    if (!selected) {
        showHint("L'id inserito non corrisponde a nessun distributore.");
        $statePanel.removeClass('visible');
        return;
    }
    showHint("Completato");

    $panelMachineId.text(machineId);
    function xmlValue(tag) {
        let el = selected.getElementsByTagName(tag)[0];
        return el ? el.textContent : null;
    }
    let state = xmlValue('state');
    $panelState.text(state);
    let stateTime = xmlValue('stateTime') || '—';
    let dt = new Date(stateTime);
    $panelUpdated.text(!isNaN(dt.getTime()) ? dt.toLocaleString() : stateTime);
    let updateMaintainer = xmlValue('updateMaintainer') || '-';
    $panelMaintainer.text(updateMaintainer);
    let $items = $errorsList.find('.error-item');
    Array.from($items).forEach(function(element, index) {
        let tagName = 'e' + (index + 1);
        let val = xmlValue(tagName);
        let isError = val === '1' ;
        let $badge = $(element).find('.status-badge');
        if(isError) {
            $badge.text('Sì');
            $badge.addClass('emergence');
        } else{
            $badge.text('No');
            $badge.removeClass('emergence');
        }
    });
    const $suppliesMap = {
        'bicchierini': '#sup-bicchierini',
        'palettine': '#sup-palettine',
        'acqua': '#sup-acqua',
        'zucchero': '#sup-zucchero',
        'caffe': '#sup-caffe',
        'latte': '#sup-latte',
        'ginseng': '#sup-ginseng',
        'cioccolata': '#sup-cioccolata',
        'vaniglia': '#sup-vaniglia'
    };
    const maxValues={
        'bicchierini': '1000',
        'palettine': '1000',
        'acqua': '10',
        'zucchero': '5',
        'caffe': '10',
        'latte': '5',
        'ginseng': '1000',
        'cioccolata': '1000',
        'vaniglia': '1000'
    }
    Object.keys($suppliesMap).forEach(function(tag) {
        let val = xmlValue(tag);
        if(val!=null){
            $( $suppliesMap[tag] ).text(val);
            if(Number(val)<=maxValues[tag]*0.2){
                $( $suppliesMap[tag] ).closest('.supply').addClass('emergence');
            } else {
                $( $suppliesMap[tag] ).closest('.supply').removeClass('emergence');
            }
        }
        else{
            $( $suppliesMap[tag] ).text('—');
            $( $suppliesMap[tag] ).closest('.supply').removeClass('emergence');
        }
    });

    $statePanel.addClass('visible');
}
