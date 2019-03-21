# Filmo

Projeto Android utilizando [TMDb API](https://www.themoviedb.org/documentation/api).

![20190320_215509](https://user-images.githubusercontent.com/39193436/54728195-2d8f8b80-4b5b-11e9-9a1a-59abd69b70de.gif)

## Definir API key no arquivo strings.xml
```xml
<string name="api_key">API_KEY_AQUI</string>
```
## Libraries
- Dagger2 para injeção de dependências
- Retrofit2 e Jackson para construção da REST API
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
