package com.udjaya.kasirudjay.model;


public class Customer {
    private int id;
    private String name;
    private String telfon;
    private int umur;
    private String email;
    private String tanggal_lahir;
    private String domisili;
    private String gender;
    private int community_id;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private int exp;
    private int point;
    private int referral_id;
    private int level_memberships_id;

    public Customer() {
    }

    public Customer(int id, String name, String telfon, int umur, String email, String tanggal_lahir, String domisili, String gender, int community_id, String deleted_at, String created_at, String updated_at, int exp, int point, int referral_id, int level_memberships_id) {
        this.id = id;
        this.name = name;
        this.telfon = telfon;
        this.umur = umur;
        this.email = email;
        this.tanggal_lahir = tanggal_lahir;
        this.domisili = domisili;
        this.gender = gender;
        this.community_id = community_id;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.exp = exp;
        this.point = point;
        this.referral_id = referral_id;
        this.level_memberships_id = level_memberships_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelfon() {
        return telfon;
    }

    public void setTelfon(String telfon) {
        this.telfon = telfon;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getDomisili() {
        return domisili;
    }

    public void setDomisili(String domisili) {
        this.domisili = domisili;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(int referral_id) {
        this.referral_id = referral_id;
    }

    public int getLevel_memberships_id() {
        return level_memberships_id;
    }

    public void setLevel_memberships_id(int level_memberships_id) {
        this.level_memberships_id = level_memberships_id;
    }
}
