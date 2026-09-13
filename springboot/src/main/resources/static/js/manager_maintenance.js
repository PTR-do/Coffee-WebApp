// Start JS
$(document).ready(function () {
    $addForm = $('#add-form');
    $removeForm = $('#remove-tech-form');
    $searchBtn = $('#search-btn');
    $cancelBtn = $('#cancel-btn');
    $searchFilter = $('#search-filter');
    $searchInput = $('#search-input');
    $resultsTable = $('#results-table');
    $resultsTbody = $('#results-table tbody');
    $noResults = $('#no-results');

    // events
    $addForm.on('submit', AddMaintainer);
    $removeForm.on('submit', RemoveMaintainer);

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
let $addForm;
let $removeForm;
let $searchBtn;
let $cancelBtn;
let $searchFilter;
let $searchInput;
let $resultsTable;
let $resultsTbody;
let $noResults;
let lastSearchResultXml = null;

//helpers
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
        overlayTimer = null; // resetta la variabile
    }, 3000);
    $overlay.find('.overlay-close').off('click').on('click', function() {
        $overlay.css('display', 'none');
        if (overlayTimer) clearTimeout(overlayTimer);
        overlayTimer = null;
    });
}
function hideResult(){
    $resultsTable.hide();
}

// add new maintainer
function AddMaintainer(event) {
    event.preventDefault()
    const $form = $(this);
    const payload = {
        name: $form.find('#add-name').val().trim(),
        location: $form.find('#add-location').val().trim(),
        date: $form.find('#add-startdate').val(),
        password: $form.find('#add-password').val().trim()
    };
    const missing = [];
    if (!payload.name) missing.push('Nome');
    if (!payload.location) missing.push('Luogo di lavoro');
    if (!payload.date) missing.push('Data di inizio lavoro');
    if (!payload.password) missing.push('Password manutentore');
    if (missing.length) {
        showOverlay('Compilare i campi obbligatori: ' + missing.join(', '), true);
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/maintenance/add',
        method: 'POST',
        timeout: 8000,
        contentType: 'application/json',
        data: JSON.stringify(payload),
        success: function (response) {
            if (response.result === true) {
                showOverlay('Manutentore aggiunto correttamente', false);
                $form[0].reset();
            } else {
                showOverlay(response.message, true);
            }
        },
        error: function () {
            showOverlay('Errore di rete.', true);
        }
    });
}

// remove a maintainer
function RemoveMaintainer(event) {
    event.preventDefault()
    const $form = $(this);
    const id = $form.find('#remove-tech-id').val().trim();
    if (!id || isNaN(Number(id))) {
        showOverlay('ID Manutentore richiesto per la rimozione', true);
        return;
    }
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/maintenance/remove',
        method: 'POST',
        timeout: 8000,
        contentType: 'application/json',
        data: JSON.stringify({
            id: id
        }),
        success: function (response) {
            if (response.result === true) {
                showOverlay('Manutentore rimosso correttamente', false);
                $form[0].reset();
            } else {
                showOverlay(response.message || 'Operazione non completata.', true);
            }
        },
        error: function () {
            showOverlay('Errore di rete.', true);
        }
    });
}

// search results
function handleSearch() {
    $.ajax({
        url: backendUrl + '/serviceSpringBoot/manager/maintenance/search',
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

//search filter for the results
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
        $(xmlDoc).find('maintenance').each(function () {
            items.push({
                id: $(this).find('id').text().trim(),
                nome: $(this).find('name').text().trim(),
                luogo: $(this).find('location').text().trim(),
                data_inizio: $(this).find('date').text().trim()
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
            if (mode === "id") {
                return value.startsWith(query);
            }
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
            $tr.append($('<td>').text(escapeHtml(row.nome)));
            $tr.append($('<td>').text(escapeHtml(row.luogo)));
            $tr.append($('<td>').text(escapeHtml(row.data_inizio)));
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
