package ru.cullxdrive.productlist;

/**
 * Класс для карточи с рецептом
 * Тут храниться назвыние, ссылка на рецепт
 * ссылка на фото и описание рецепта
 */
public class RecipeItem {
    private int     id;
    private boolean favorites;
    private String  url;
    private String  name;
    private String  description;
    private String  imageUrl;
    private String  ingredient;
    private String  instruction;


    public String getInstruction() {
        return instruction;
    }

    public String getUrl(){
        return url;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public int getId(){
        return id;
    }

    public boolean isFavorites(){
        return favorites;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
