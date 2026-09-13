// Start JS
$(document).ready(function () {
    $addForm = $('#add-distributor-form');
    $removeForm = $('#remove-form');
    $activateForm = $('#activate-form');
    $deactivateForm = $('#deactivate-form');
    $searchBtn = $('#search-btn');
    $cancelBtn = $('#cancel-btn');
    $searchFilter = $('#search-filter');
    $searchInput = $('#search-input-dist');
    $resultsTable = $('#results-table-dist');
    $resultsTbody = $('#results-table-dist tbody');
    $noResults = $('#no-results-dist');

    loadMap();

    // events
    map.on('click', enableMapClick)

    $addForm.on('submit', AddDistributor);
    $removeForm.on('submit', RemoveDistributor);
    $activateForm.on('submit', ActivateDistributor);
    $deactivateForm.on('submit', DeactivateDistributor);

    $searchBtn.on('click', handleSearch);
    $cancelBtn.on('click', hideResult);

    $searchFilter.on('change', function () {
        if (lastSearchResultXml) searchFilter();
    });
    $searchInput.on('input', function () {
        if (lastSearchResultXml) searchFilter();
    });
});
//---

const backendUrl = "http://localhost:9090";
let $addForm, $removeForm, $activateForm, $deactivateForm;
let $searchBtn, $cancelBtn, $searchFilter, $searchInput;
let $resultsTable, $resultsTbody, $noResults;
let map;
let lastSearchResultXml = null;

// helper functions
let overlayTimer = null;
function showOverlay(message, isError) {
    const $overlay = $('#backend-overlay-layer');
    const $panel = $overlay.find('.backend-overlay-panel');
    const $msg = $overlay.find('.backend-overlay-message');
    $msg.text(message || '');
    $panel.css({
        'background-color': isError ? '#6c2d2d' : '#f7fff7',
    });
    $overlay.css('display', 'flex');
    if (overlayTimer) clearTimeout(overlayTimer);
    overlayTimer = setTimeout(() => {
        $overlay.css('display', 'none');
        overlayTimer = null;
    }, 3000);
    $overlay.find('.overlay-close').off('click').on('click', function() {
        $overlay.css('display', 'none');
        if (overlayTimer) clearTimeout(overlayTimer);
        overlayTimer = null;
    });
}
function hideResult() {
    $resultsTable.hide();
}

//map loading
function loadMap(){
    map = L.map('map').setView([38.115681, 13.361440], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18
    }).addTo(map);
}
//click location
let lat = null;
let lng = null;
function enableMapClick(e) {
    lat = e.latlng.lat;
    lng = e.latlng.lng;
    $("#lat").text("Lat: " + lat.toFixed(6));
    $("#lng").text("Lng: " + lng.toFixed(6));
}

// add new distributor
function AddDistributor(event) {
    event.preventDefault();
    const $form = $(this);
    const payload = {
        location: $form.find('#add-dist-location').val().trim(),
        date: $form.find('#add-dist-date').val(),
        state: $form.find('#add-dist-status').val(),
        lat: lat,
        lng: lng
    };
    if (!payload.location || !payload.date || !payload.state) {
        showOverlay('Compilare i dati di inserimento obbligatori', true);
        return;
    }
    if (!lat) {
        showOverlay('Localizzare la posizione del distributore nella mappa', true);
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/distributor/add',
        method: 'POST',
        timeout: 8000,
        contentType: 'application/json',
        data: JSON.stringify(
            payload
        ),
        success: function (response) {
            if (response.result === true) {
                showOverlay('Distributore aggiunto correttamente', false);
                $form[0].reset();
            } else {
                showOverlay(response.message, true);
            }
        },
        error: function (response) {
            showOverlay(response.message || 'Errore di rete.', true);
        }
    });
}

// remove distributor
function RemoveDistributor(event) {
    event.preventDefault();
    const $form = $(this);
    const id = $form.find('#remove-id').val().trim();
    if (!id || isNaN(Number(id))) {
        showOverlay('ID distributore richiesto per la rimozione', true);
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/distributor/remove',
        method: 'POST',
        timeout: 8000,
        contentType: 'application/json',
        data: JSON.stringify({
            id: id
        }),
        success: function (response) {
            if (response.result === true) {
                showOverlay('Distributore rimosso correttamente', false);
                $form[0].reset();
            } else {
                showOverlay(response.message || 'Operazione non completata.', true);
            }
        },
        error: function (response) {
            showOverlay(response.message || 'Errore di rete.', true);
        }
    });
}

// activate distributor
function ActivateDistributor(event) {
    event.preventDefault();
    const $form = $(this);
    const id = $form.find('#activate-id').val().trim();
    if (!id || isNaN(Number(id))) {
        showOverlay('ID distributore richiesto per l\'attivazione', true);
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/distributor/activate',
        method: 'POST',
        timeout: 8000,
        contentType: 'application/json',
        data: JSON.stringify({
            id: id
        }),
        success: function (response) {
            if (response.result === true) {
                showOverlay('Distributore attivato correttamente', false);
                $form[0].reset();
            } else {
                showOverlay(response.message || 'Operazione non completata.', true);
            }
        },
        error: function (response) {
            showOverlay(response.message || 'Errore di rete.', true);
        }
    });
}

// deactivate distributor
function DeactivateDistributor(event) {
    event.preventDefault();
    const $form = $(this);
    const id = $form.find('#deactivate-id').val().trim();
    const status = $form.find('input[name="distributor-status"]:checked').val();
    if (!id || isNaN(Number(id))) {
        showOverlay('ID distributore richiesto per la disattivazione', true);
        return;
    }
    if (!status) {
        showOverlay('Selezionare lo stato del distributore disattivato', true);
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/distributor/deactivate',
        method: 'POST',
        timeout: 8000,
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            state: status
        }),
        success: function (response) {
            if (response.result === true) {
                showOverlay('Distributore disattivato correttamente', false);
                $form[0].reset();
            } else {
                showOverlay(response.message || 'Operazione non completata.', true);
            }
        },
        error: function (response) {
            showOverlay(response.message || 'Errore di rete.', true);
        }
    });
}

// search results
function handleSearch() {
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/distributor/search',
        method: 'GET',
        timeout: 8000,
        dataType: 'xml',
        success: function (response) {
            lastSearchResultXml = tryParseXml(response);
            searchFilter();
        },
        error: function () {
            showOverlay('Errore di rete.', true);
        }
    });

    function tryParseXml(response) {
        if (!response) return null;
        if (response instanceof XMLDocument) return response;
        try {
            return (new DOMParser()).parseFromString(response, "application/xml");
        } catch {
            return null;
        }
    }
}

// search filter for distributors
function searchFilter() {
    let items = parseXml(lastSearchResultXml);
    if (items.length === 0) {
        showOverlay('Nessun risultato.', false);
        return;
    }
    const mode = $searchFilter.val();
    const text = $searchInput.val().trim();
    items = sortItems(items, mode);
    if (text) items = filterItems(items, mode, text);
    populateResultsTable(items);

    function parseXml(xmlDoc) {
        if (!xmlDoc) return [];
        const items = [];
        $(xmlDoc).find('distributor').each(function () {
            items.push({
                id: $(this).find('id').text().trim(),
                locazione: $(this).find('location').text().trim(),
                data: $(this).find('date').text().trim(),
                stato: $(this).find('state').text().trim()
            });
        });
        return items;
    }

    function sortItems(items, mode) {
        if (mode === "all") return items;
        return items.sort((a, b) => {
            const A = a[mode] || "";
            const B = b[mode] || "";
            if (mode === "id") {
                return Number(A) - Number(B);
            }
            return String(A).localeCompare(String(B));
        });
    }

    function filterItems(items, mode, text) {
        if (!text || mode === "all") return items;
        const query = text.toLowerCase().trim();
        return items.filter(item => {
            const value = (item[mode] || "").toLowerCase();
            if (mode === "id") return value.startsWith(query);
            const words = value.split(" ");
            return words.some(word => word.startsWith(query));
        });
    }

    function populateResultsTable(items) {
        $resultsTbody.empty();
        if (!items.length) {
            $resultsTable.hide();
            $noResults.show();
            return;
        }
        $noResults.hide();
        $resultsTable.show();
        items.forEach(function (row) {
            const $tr = $('<tr>');
            $tr.append($('<td>').text(escapeHtml(row.id)));
            $tr.append($('<td>').text(escapeHtml(row.locazione)));
            $tr.append($('<td>').text(escapeHtml(row.data)));
            $tr.append($('<td>').text(escapeHtml(row.stato)));
            $resultsTbody.append($tr);
        });
    }

    function escapeHtml(str) {
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }
}



