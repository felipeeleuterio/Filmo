# Filmo

Projeto Android utilizando [TMDb API](https://www.themoviedb.org/documentation/api).

![20190320_222050](https://user-images.githubusercontent.com/39193436/54728799-b825ba00-4b5e-11e9-8783-5fb4362d9b74.gif)
![20190320_220502](https://user-images.githubusercontent.com/39193436/54728545-3d0fd400-4b5d-11e9-9eec-bd68047252e2.gif)
![20190320_220558](https://user-images.githubusercontent.com/39193436/54728588-895b1400-4b5d-11e9-900b-1569329638b3.gif)

## Definir API Key no arquivo local.properties
```xml
tmdb_api_key=API_KEY_AQUI
```
## Libraries
- Dagger2 para injeção de dependências
- Retrofit2, RxJava2 e Jackson para construção da REST API
- OkHttp3 para auxiliar na recepção da REST API
- Android Architecture Components: Room para persistência de dados e funcionamento offline
- Glide para carregar as imagens
- Butter Knife para manipulação das Views
- Stetho para depurar dados de persistência e pacotes de rede
- Mockito/JUnit para testes

## Destaques

### Lista de filmes a [estrear](https://developers.themoviedb.org/3/movies/get-upcoming)
- Pull to Refresh
- Endless Scroll

### [Procurar](https://developers.themoviedb.org/3/search/search-movies) filmes
- Toolbar para pesquisar filmes pelo nome

### Detalhes do filme
- Informações sobre: sinopse, gêneros, data de estréia, capa do filme
