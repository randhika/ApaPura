package md.fusionworks.aquamea.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;

public class HealthAndTreatmentActivity extends BaseNavigationDrawerActivity {

    @Bind(R.id.healthField)
    TextView healthField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_treatment);

        ButterKnife.bind(this);

        String html = "<html><body>" +
                "<b>Ce se intampla in corp cand bem apa?</b><br/><br/>" +
                "Ralph Vornehm a prezentat componenta de apa a fiecarui organ din corpul omenesc, incepand cu celulele, sangele, neurotransmitatorii si terminand cu organele.\n" +
                "Astfel, creierul contine 90% apa, ceea ce explica intr-un fel capacitatea acestuia de a transmite mesajele catre toate organele cu viteze uluitoare; neurotransmitatorii - responsabili de transmiterea acestor mesaje - contin tot 90% apa.\n" +
                "Este de inteles atunci, de ce cand nu bem apa, nu mai simtim cu timpul senzatia de sete. Neurotransmitatorii deshidratati nu mai transmit corect mesajul \"mi-e sete\", asa incat setea incepe sa mistuie corpul, incep durerile (si ele semnale de deshidratare) si patologia multor organe. \"Celulele corpului nostru nu se ating intre ele, pentru ca inoata in apa, la fel ca si nervii\"- R. Vornehm\n" +
                "\n" +
                "<br/><br/><b>Ce se intampla cand apa din corp este murdara?</b><br/><br/></body></html>"
                + "Celulele comunica printr-un sistem de tip bio-laser. Exista un tip de apa in interiorul celulei si alt tip de apa in afara ei. Schimbul intre acestea purifica celula si o ajuta sa functioneze corect.\n" +
                "Daca nu bem apa regulat, celulele se intoxica. Cind membrana unei celule nu este bine hidratata, corpul declanseaza un mecanism de urgenta, ca forma de aparare si de hidratare. Corpul se protejeaza de deshidratare cu ajutorul colesterolului.\n" +
                "Membrana se imbraca intr-o pelicula protectoare, ca o crema, care impiedica uscarea. Ce este aceasta pelicula? Surpriza, este colesterol.\n"
                + "<br/><br/><b>Corpul nostru se protejeaza de deshidratare cu ajutorul colesterolului</b><br/><br/>" +

                "Studiile efectuate au aratat ca, in 90% din cazuri, colesterolul scade in 21 de zile de baut apa corect, ceea ce inseamna ca in 90% din cazuri colesterolul este crescut din cauza deshidratarii.\n" +
                "Ralph Vornehm arata ca celulele deshidratate se afla intr-un fel de sfera intunecata, dupa care devin mutante si se ajunge la cancer (studiile facute pe celulele canceroase au aratat ca acestea nu mai comunica intre ele).\n"
                + "<br/><br/><b>La nivelul cartilagiilor, componenta apei este de 90%</b><br/><br/>" +

                "Cand cartilagiul este deshidratat, incepe sa pocneasca. Oasele au nevoie de apa in aceeasi masura si multe dintre durerile de oase, precum si sindromul de alunecare al discului lombar sunt cauzate de deshidratare. Tensiunea arteriala mare, problemele legate de vasele de sange sunt cauzate de lipsa apei din corp.\n" +
                "Presupunem, insa, ca durerile sunt mai mult decat o rugaminte pe care corpul ne-o trimite.\n" +
                "Durerea trebuie sa fie un mare strigat de ajutor, care s-ar putea traduce: \"Ajutor, mi-e sete de nu mai pot!\""
                + "<br/><br/><b>Metode de tratament cu apa</b><br/><br/>" +

                "Beti apa curata si ramaneti sanatosi si activi.\n" +
                "Nu beti apa la masa!\n" +
                "\n" +
                "\n" +
                "Celor carora le place sa bea apa rece, la masa, le sunt adresate aceste randuri: Apa rece va solidifica mancarurile uleioase pe care tocmai le-ati consumat.\n" +
                "Va va incetini digestia. Odata ce acest \"noroi\" reactioneaza cu acidul din stomac, va fi absorbit mai repede decat hrana solida. Va depune un strat pe intestine.\n" +
                "Foarte curand, acesta se va transforma in grasime si reziduuri si va duce la cancer.\n" +
                "\n" +
                "Este cel mai indicat sa beti supa fierbinte sau apa calda dupa masa.\n" +
                "O nota importanta despre atacurile de inima:\n" +
                "\n" +
                "\n" +
                "Ar trebui sa se stie ca nu fiecare simptom de atac de inima va fi durerea in mana stanga.\n" +
                "Fiti atenti la durerile intense in maxilar.\n" +
                "S-ar putea sa nu ai prima data durere in piept, in timpul unui atac de inima.\n" +
                "Greata si transpiratul intens sunt si ele simptome obisnuite. 60% din persoanele care au atac de inima in timp ce dorm, nu se trezesc...\n" +
                "Durerea in maxilar va poate trezi dintr-un somn adanc. Fiti cu grija si atenti... Cu cat stim mai multe, cu atat sansele de a supravietui sunt mai mari.\n" +
                "Un cardiolog spunea ca, daca fiecare din cei care primesc acest mail, il trimite la toti cunoscutii sai, putem fi siguri ca vom salva cel putin o viata.\n";

        healthField.setText(Html.fromHtml(html));
    }

    @Override
    protected int getSelfDrawerItem() {

        return DRAWER_ITEM_HEALTH_TREATMENT;
    }
}
