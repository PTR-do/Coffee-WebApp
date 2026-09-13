// Start JS
$(document).ready(function () {
    loadMap();
    loadPoint()
});
//---

const backendUrl = "http://localhost:8080";
let map;

//map loading
function loadMap(){
    map = L.map('map').setView([38.115681, 13.361440], 15);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18
    }).addTo(map);
}

//distributor positions loading
function loadPoint() {
    $.ajax({
        url: backendUrl + '/servicejakarta/loadPosition',
        method: 'GET',
        dataType: 'json',
        timeout: 8000,
        success: function (response) {
            if (!response || response.result !== true) {
                alert('Impossibile caricare i dati.');
                return;
            }
            console.log("Points ricevuti:", response.data);
            response.data.forEach(p => {
                const color = getMarkerColor(p.stato);
                const markerIcon = L.divIcon({
                    className: 'custom-marker',
                    html: `<div class="marker-dot" style="background-color:${color}"></div>`,
                    iconSize: [16, 16],
                    iconAnchor: [8, 8]
                });
                L.marker([p.lat, p.lng], { icon: markerIcon })
                    .addTo(map)
                    .bindPopup(`
                        <b>${p.posizione}</b><br>
                        Stato: ${p.stato.replace('_', ' ')}
                    `);
            });
        },
        error: function () {
            alert('Errore di rete.');
        }
    });

    function getMarkerColor(stato) {
        switch (stato) {
            case 'attivo':
                return 'blue';
            case 'non_attivo':
                return 'gray';
            case 'in_manutenzione':
                return 'yellow';
            case 'guasto':
                return 'red';
            case 'rimosso':
                return 'red';
        }
    }
}



