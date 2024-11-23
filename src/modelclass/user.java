package modelclass;

public class user {

    private int id_user;
    private String name_user;
    private String email_user;
    private String password_user;

    private String pesquisa;

    private int id_perm; // campo de permissão

    private String desc_perm;
    private permission perm;

    public user() {
    }

    public user(int id_user, String name_user, String email_user, String password_user, int id_perm) {
        this.id_user = id_user;
        this.name_user = name_user;
        this.email_user = email_user;
        this.password_user = password_user;
        this.id_perm = id_perm;
    }

    public user(int id_user, String name_user, String email_user, String password_user, permission perm) {
        this.id_user = id_user;
        this.name_user = name_user;
        this.email_user = email_user;
        this.password_user = password_user;
        this.perm = perm;
    }

    public boolean possuipermissao(){
        return getPerm() != null;
    }

    /**
     * @return the id_user
     */
    public int getId_user() {
        return id_user;
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    /**
     * @return the name_user
     */
    public String getName_user() {
        return name_user;
    }

    /**
     * @param name_user the name_user to set
     */
    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    /**
     * @return the email_user
     */
    public String getEmail_user() {
        return email_user;
    }

    /**
     * @param email_user the email_user to set
     */
    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    /**
     * @return the password_user
     */
    public String getPassword_user() {
        return password_user;
    }

    /**
     * @param password_user the password_user to set
     */
    public void setPassword_user(String password_user) {
        this.password_user = password_user;
    }

    /**
     * @return the pesquisa
     */
    public String getPesquisa() {
        return pesquisa;
    }

    /**
     * @param pesquisa the pesquisa to set
     */
    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }

    /**
     * @return the id_perm
     */
    public int getId_perm() {
        return id_perm;
    }

    /**
     * @param id_perm the id_perm to set
     */
    public void setId_perm(int id_perm) {
        this.id_perm = id_perm;
    }

    /**
     * @return the desc_perm
     */
    public String getDesc_perm() {
        return desc_perm;
    }

    /**
     * @param desc_perm the desc_perm to set
     */
    public void setDesc_perm(String desc_perm) {
        this.desc_perm = desc_perm;
    }

    /**
     * @return the perm
     */
    public permission getPerm() {
        return perm;
    }

    /**
     * @param perm the perm to set
     */
    public void setPerm(permission perm) {
        this.perm = perm;
    }
   
}
