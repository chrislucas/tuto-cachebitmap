package components.com.project.efficientlybitmaps.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by r028367 on 17/07/2017.
 * https://developer.android.com/topic/performance/graphics/load-bitmap.html#load-bitmap
 * https://developer.android.com/topic/performance/graphics/load-bitmap.html
 * https://stuff.mit.edu/afs/sipb/project/android/docs/training/displaying-bitmaps/load-bitmap.html#decodeSampledBitmapFromResource
 */

public class BitmapUtils {

    /**
     *
     * Como ler dimensao e tipo de imagem
     *
     * A classe  BitmapFactory  prove varios metodos de decodificacao de bitmaps
     *  (decodeByteArray(), decodeFile(), decodeResource(), etc.) , para cria-los
     *  vindos de diversar fontes.
     *
     *  Esse metodos tentam alocar memoria para criar o bitmap, porem podem facilmente
     *  lançar uma excecao de OutOfMemory.
     *
     *
     * */

    public static class MetadataBitmap {
        private int h, w;
        private String type;
        public MetadataBitmap(int h, int w, String type) {
            this.h = h;
            this.w = w;
            this.type = type;
        }
    }

    /**
     *
     * Esse metodo pode ser usado da seguinte forma
     *
     * myImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.bitmap, 100, 100));
     *
     * */
    public static Bitmap decodeBitmapFromResource(Resources resource, int idResource
            , int reqWidth, int reqHeight) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        /**
         * Definir BitmapFactory.Options.inJustDecodeBounds = true no momento de decodificacao
         * de uma imagem evita o alocamento de memoria, retornando um objeto nulo, porem
         * as dimensoes da imagem sao definidas
         *
         * */
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resource, idResource, bitmapOptions);
        // Dimensoes e tipo da image
        int imageHeight     = bitmapOptions.outHeight;
        int imageWidth      = bitmapOptions.outWidth;
        String imageType    = bitmapOptions.outMimeType;
        Log.i("DATA_IMAGE", String.format("(%d, %d) -> Tipo %s", imageHeight, imageWidth, imageType));
        /**
         * Com os dados sobre a imagem
         * calculamos o valor da reduzido da imagem para alocar menos memoria para armazena-la
         * */
        bitmapOptions.inSampleSize = calculateSampleSize2(bitmapOptions, reqWidth, reqHeight);
        bitmapOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resource, idResource, bitmapOptions);
    }

    /**
     * Sabendo as dimensões da imagem podemos decidir se a carregamos na memoria
     * ou se criamos uma versão reduzida dela para carregarmos.
     *
     * Informacoes a se considerar
     * 1) Estimar a quantidade de memoria para carregar a imagem inteira
     * 2) Quantidade de memoria que se esta disposto a gastar para carregar a imagem
     * 3) Dimensoes da View (ImageView por exemplo) que recebera o bitmap
     * 4) Screen Size e densisadade de tela do dispositivo
     *
     * */
    public static int calculateSampleSize(BitmapFactory.Options options, int reqWidth
            , int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int sampleSize = 1;
        if(h > reqHeight || w > reqWidth) {
            int hRatio = Math.round((float) h / (float) reqHeight);
            int wRatio = Math.round((float) w / (float) reqWidth);
            sampleSize = hRatio < wRatio ? hRatio : wRatio;
        }
        return sampleSize;
    }

    /**
     * Para fazer o decodificador carregar uma versão reduzida da imagem em memoria
     * defina inSampleSize com um valor > 0 em BitmapFactory.Options.inSampleSize
     * */
    public static int calculateSampleSize2(BitmapFactory.Options options, int reqWidth
            , int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int sampleSize = 1;
        if(h > reqHeight || w > reqWidth) {
            int halfH = h / 2;
            int halfW = w / 2;
            while( (halfH / sampleSize) >= reqHeight
                    && (halfW / sampleSize) >= reqWidth ) {
                // definindo tamanho da imagem em multiplos de 2
                /**
                 * A power of two value is calculated because the decoder uses a final
                 * value by rounding down to the nearest power of two, as per the inSampleSize documentation.
                 * https://developer.android.com/reference/android/graphics/BitmapFactory.Options.html#inSampleSize
                 * */
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    public static MetadataBitmap checkImage(Context context, int id) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id, bitmapOptions);
        int imageHeight     = bitmapOptions.outHeight;
        int imageWidth      = bitmapOptions.outWidth;
        String imageType    = bitmapOptions.outMimeType;
        MetadataBitmap metadata = new MetadataBitmap(imageHeight, imageWidth, imageType);
        return metadata;
    }
}
