package components.com.project.efficientlybitmaps.activities;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import components.com.project.efficientlybitmaps.R;
import components.com.project.efficientlybitmaps.activities.fragments.ImageGridFragment;


/**
 * Artigo interessante
 * https://developer.android.com/topic/performance/graphics/cache-bitmap.html
 *
 * Carregar uma imagem num ImageView eh uma tarefa simples. As situação começa
 * a complicar quando queremos carregar uma quantidade grande de imagens num listview
 * , gridview ou viewpager.
 *
 * Para manter o uso baixo de memoria, reciclar as views que não aparecem mais na
 * tela, como vemos no padrao ViewHolder numa listview eh uma tecnica muito boa.
 * O coletor de lixo libera memoria quando entende que uma imagem bão sera mais usada.
 * Mas remover imagens da memoria todas as vezes que ela some da tela, e realocar memoria
 * sempre que a imagem aparece não eh uma maneira eficiente. Essa tecnica acaba quebrando
 * a velocidade e fluidez da UI. Para contornar isso usamos memory cache e disk cache.
 *
 * */

/**
 * Memory Cache
 * Carregamento rapido de dados em troca de maior uso da memoria
 * Usar {@link LruCache}
 * Permite armazenas referencias recentes numa estrutura de {@link java.util.LinkedHashMap}
 * eliminando o objeto mais antigo antes do cache exceder seu limite
 * */
public class ImageGridActivity extends AppCompatActivity {

    private ImageGridFragment imageGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        imageGridFragment = ImageGridFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.temp_image_grid_layout, imageGridFragment);
        fragmentTransaction.commit();

    }
}
