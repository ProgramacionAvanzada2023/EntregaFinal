package ClasesPrincipales;

import java.util.ArrayList;
import java.util.List;

public class RecSys {
    private Algorithm algorithm;
    private Table testData;
    private List<String> testItemNames;
    private List<Integer> listEstimate = new ArrayList<>();

    public RecSys(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void train(Table trainData) {
        this.algorithm.train(trainData);
    }

    public void run(Table testData, List<String> testItemNames) {
        this.testData = testData;
        this.testItemNames = testItemNames;
        for (int i = 0; i < testData.getNumeroDeFilas(); i++) {
            int estimatedClass = (int) algorithm.estimate(testData.getRowAt(i).getFila());
            listEstimate.add(estimatedClass);
        }
    }

    private List<String> selectItems(int indiceLikedItem, int labelLikedItem, int numRec){
        List<String> resultadoItems = new ArrayList<>();
        if(indiceLikedItem == -1) { return resultadoItems; }
        int numActual=0;
        for (int j = 0; j < listEstimate.size(); j++){
            if(listEstimate.get(j) == labelLikedItem && numActual < numRec && j!= indiceLikedItem) {
                resultadoItems.add(testItemNames.get(j));
                numActual++;
            }
        }
        return resultadoItems;
    }

    public List<String> recommend(String nameLikedItem, int numRecommendations){
        List<String> listRecommend = new ArrayList<String>();
        int indiceLikedItem = findName(nameLikedItem);
        if(indiceLikedItem >= 0 && indiceLikedItem < testItemNames.size()) {
            int labelLikedItem = listEstimate.get(indiceLikedItem);
            listRecommend = selectItems(indiceLikedItem, labelLikedItem, numRecommendations);
        }
        return listRecommend;
    }

    private int findName(String nameItem){
        int indice = -1;
        for (int i = 0; i < testItemNames.size();i++){
            if (testItemNames.get(i).equals(nameItem)) {
                indice = i;
                break;
            }
        }
        return indice;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
