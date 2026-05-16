package br.ufal.ic.myfood.models;

public class Mercado extends Empresa{
    private String abre;
    private String fecha;
    private String tipoMercado;

    public Mercado() {}

    public Mercado(int id, int dono, String nome, String endereco, String abre, String fecha, String tipoMercado) {
        super(id, dono, nome, endereco);
        this.abre = abre;
        this.fecha = fecha;
        this.tipoMercado = tipoMercado;
    }

    public String getAbre() {return abre;}
    public String getFecha() {return fecha;}
    public String getTipoMercado() {return tipoMercado;}

    public void setAbre(String abre) {this.abre = abre;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    public void setTipoMercado(String tipoMercado) {this.tipoMercado = tipoMercado;}

    public static void validarHorario(String abre, String fecha) throws Exception {
        if (abre == null || fecha == null) throw new br.ufal.ic.myfood.exceptions.Empresas.Mercado.HorarioInvalido();

        if (abre.length() != 5 || fecha.length() != 5 || abre.charAt(2) != ':' || fecha.charAt(2) != ':'){
            throw new br.ufal.ic.myfood.exceptions.Empresas.Mercado.FormatoHoraInvalido();
        }

        try {
            int hAbre = Integer.parseInt(abre.substring(0, 2));
            int mAbre = Integer.parseInt(abre.substring(3, 5));
            int hFecha = Integer.parseInt(fecha.substring(0, 2));
            int mFecha = Integer.parseInt(fecha.substring(3, 5));

            if (hAbre < 0 || hAbre > 23 || mAbre < 0 || mAbre > 59 || hFecha < 0 || hFecha > 23 || mFecha < 0 || mFecha > 59) {
                throw new br.ufal.ic.myfood.exceptions.Empresas.Mercado.HorarioInvalido();
            }

            if (abre.compareTo(fecha) >= 0) {
                throw new br.ufal.ic.myfood.exceptions.Empresas.Mercado.HorarioInvalido();
            }
        } catch (NumberFormatException e) {
            throw new br.ufal.ic.myfood.exceptions.Empresas.Mercado.FormatoHoraInvalido();
        }
    }
}
