
package modelclass;


public class permission {
   private int id_perm;
    private String desc_perm;

     // Construtor com argumentos
    public permission(int id_perm, String desc_perm) {
        this.id_perm = id_perm;
        this.desc_perm = desc_perm;
    }

    // Construtor vazio (opcional, dependendo do uso)
    public permission() {
    }
    
    // Getters e Setters
    public int getId_perm() {
        return id_perm;
    }

    public void setId_perm(int id_perm) {
        this.id_perm = id_perm;
    }

    public String getDesc_perm() {
        return desc_perm;
    }

    public void setDesc_perm(String desc_perm) {
        this.desc_perm = desc_perm;
    }
}
