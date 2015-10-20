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

        String htmlText;
        String languageCode = getString(R.string.language);
        switch (languageCode) {

            case "ro":

                htmlText = getRoHtmlText();
                break;
            case "ru":

                htmlText = getRuHtmlText();
                break;
            case "en":

                htmlText = getEnHtmlText();
                break;
            default:
                htmlText = getEnHtmlText();
        }

        healthField.setText(Html.fromHtml(htmlText));
    }

    @Override
    protected int getSelfDrawerItem() {

        return DRAWER_ITEM_HEALTH_TREATMENT;
    }

    private String getRoHtmlText() {

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
                "Ralph Vornehm arata ca celulele deshidratate se afla intr-un fel de sfera intunecata, dupa care devin mutante si se ajunge la cancer (studiile facute pe celulele canceroase au aratat ca acestea nu mai comunica intre ele).\n";

        return html;
    }

    private String getRuHtmlText() {

        String html = "<html><body>" +
                "<b>Вода – основа существования всего живого на планете</b><br/><br/>" +

                "Запасы воды в человеческом организме нужно восполнять ежедневно, так как она, являясь незаменимым участником обменных процессов, постоянно расходуется на различные нужды нашего тела." +
                " Однако не многие задумываются над тем, что пополнять водные запасы также нужно правильно, ведь некоторые общеизвестные факты о воде являются мифами." +

                "<br/><br/><b>Вода выводит токсины</b><br/><br/></body></html>"

                + "Вода не влияет на выведение токсинов." +
                " Моча позволяет освободить организм человека от мочевины, а также некоторых ядовитых веществ и медикаментов." +
                " Однако, какое бы количество жидкости ни перерабатывалось, почки задерживают нужные организму молекулы, а остальные пропускают." +
                " Моча просто будет иметь большую или меньшую концентрацию." +
                " Так что если мы будем больше пить, то вода, циркулирующая в нашем теле, от этого не станет чище."

                + "<br/><br/><b>Вода помогает похудеть</b><br/><br/>" +

                "«Вода не растворяет ни жиры, ни сахара и не вызывает потерю жировой массы», — утверждает диетолог Арно Бадеван." +
                " Вода - это просто символ очищения, который используется в маркетинге и рекламе. " +
                "Если диетологи и говорят своим пациентам, чтобы те не забывали пить, то лишь затем, чтобы не снижать поступление в организм воды, снижая потребление калорий." +
                " «А если кто-то не может обходиться без бутылки с водой, когда сидит на диете, то это скорее оттого, что хочет заглушить голод, — объясняет он. — Это своего рода ритуал».";

        return html;
    }

    private String getEnHtmlText() {
        String html = "<html><body>" +
                "<b>Drinking water flushes toxins from your body</b><br/><br/>" +

                "Though water doesn’t necessarily neutralize toxins, the kidneys do use water to get rid of certain waste products." +
                " If you don’t drink enough water, your kidneys don’t have the amount of fluid they need to do their job properly." +
                " “If the body does not have sufficient water, then metabolic wastes will not be removed as efficiently as they should,” explains Amy Hess-Fischl, RD, CDE, of the University of Chicago Kovler Diabetes Center." +
                " “In essence, the body would be holding in toxins instead of expelling them, as is required for proper health.”" +

                "<br/><br/><b>Drinking water helps you lose weight</b><br/><br/></body></html>"

                + "Drinking water won’t specifically trigger weight loss, but it can aid in the process.\n" +
                " Water replaces other calorie-laden beverages in the diet, causing you to reduce your overall number of calories.\n" +
                " Plus, it can make you feel fuller, so you may eat less at each meal.\n" +
                " Water, particularly cold water, may even play a role in increasing your metabolism.\n" +
                " “A new study seems to indicate that drinking water actually speeds up weight loss,” says Tanya Zuckerbrot, MS, RD, owner of Tanya Zuckerbrot Nutrition, LLC, in New York City.\n" +
                " “Researchers in Germany found that subjects of the study increased their metabolic rates [or the rate at which calories are burned] by 30 percent after drinking approximately 17 ounces of water.”"

                + "<br/><br/><b>It’s possible to drink too much water</b><br/><br/>" +

                "People with certain health conditions can put themselves at risk of complications if they drink too much water. " +
                "“People with some heart conditions, high blood pressure, or swelling of the lower legs [edema] need to avoid excess water,” says Hess-Fischl." +
                " “If you have a history of kidney problems, especially if you have had a transplant, consult your doctor before increasing your fluid intake.” Hess-Fischl adds that you shouldn't drink too much water while eating, as it dilutes your stomach acid and can cause digestion problems.";

        return html;
    }
}
