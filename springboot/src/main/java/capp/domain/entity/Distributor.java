package capp.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "distributor")
public class Distributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private LocalDate date;
    private double lat;
    private double lng;
    private boolean notify;

    @Enumerated(EnumType.STRING)
    private State state;
    private LocalDateTime stateTime;
    private String updateMaintainer;
    private Boolean e1;
    private Boolean e2;
    private Boolean e3;
    private Boolean e4;
    private Boolean e5;
    private Integer bicchierini;
    private Integer palettine;
    private Double acqua;
    private Double zucchero;
    private Double caffe;
    private Double latte;
    private Integer ginseng;
    private Integer cioccolata;
    private Integer vaniglia;

    // Costruttore per la creazione (Anagrafica)
    public Distributor(String location, LocalDate date, double lat, double lng, boolean notify, State state) {
        this.location = location;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.notify = notify;
        this.state = state;
    }
    public Distributor() {
    }

    // Getter e Setter Anagrafica
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public boolean isNotify() { return notify; }
    public void setNotify(boolean notify) { this.notify = notify; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    // Getter e Setter Operativi
    public LocalDateTime getStateTime() { return stateTime; }
    public void setStateTime(LocalDateTime stateTime) { this.stateTime = stateTime; }

    public String getUpdateMaintainer() { return updateMaintainer; }
    public void setUpdateMaintainer(String updateMaintainer) { this.updateMaintainer = updateMaintainer; }

    public Boolean getE1() { return e1; }
    public void setE1(Boolean e1) { this.e1 = e1; }

    public Boolean getE2() { return e2; }
    public void setE2(Boolean e2) { this.e2 = e2; }

    public Boolean getE3() { return e3; }
    public void setE3(Boolean e3) { this.e3 = e3; }

    public Boolean getE4() { return e4; }
    public void setE4(Boolean e4) { this.e4 = e4; }

    public Boolean getE5() { return e5; }
    public void setE5(Boolean e5) { this.e5 = e5; }

    public Integer getBicchierini() { return bicchierini; }
    public void setBicchierini(Integer bicchierini) { this.bicchierini = bicchierini; }

    public Integer getPalettine() { return palettine; }
    public void setPalettine(Integer palettine) { this.palettine = palettine; }

    public Double getAcqua() { return acqua; }
    public void setAcqua(Double acqua) { this.acqua = acqua; }

    public Double getZucchero() { return zucchero; }
    public void setZucchero(Double zucchero) { this.zucchero = zucchero; }

    public Double getCaffe() { return caffe; }
    public void setCaffe(Double caffe) { this.caffe = caffe; }

    public Double getLatte() { return latte; }
    public void setLatte(Double latte) { this.latte = latte; }

    public Integer getGinseng() { return ginseng; }
    public void setGinseng(Integer ginseng) { this.ginseng = ginseng; }

    public Integer getCioccolata() { return cioccolata; }
    public void setCioccolata(Integer cioccolata) { this.cioccolata = cioccolata; }

    public Integer getVaniglia() { return vaniglia; }
    public void setVaniglia(Integer vaniglia) { this.vaniglia = vaniglia; }
}