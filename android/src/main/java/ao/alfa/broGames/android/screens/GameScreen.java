package ao.alfa.broGames.android.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import ao.alfa.broGames.android.YouGame;
import ao.alfa.broGames.android.draw.MyDraw;
import ao.alfa.broGames.android.lego.Player;
import ao.alfa.broGames.android.utils.UtilsGame;

public class GameScreen implements Screen {
    final YouGame game;
    private ShapeRenderer shapeRenderer;
    private List<Vector2> caminhoAnimacao = new ArrayList<>();
    private final List<Vector2> pathPoints = new ArrayList<>();

    private Vector2 bolaPosicao;
    private int caminhoIndex = 0;
    private float velocidade = 400f; // pixels por segundo
    private boolean bolaEmMovimento = false;

    //Players
    Player player = new Player(100, 100, 100, 200);

    OrthographicCamera camera;


    public GameScreen(YouGame game) {
        this.game = game;
        // Criar a câmera com largura e altura do mundo
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800); // Modo vertical (false), largura, altura

    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();


        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPos = new Vector3(screenX, screenY, 0);
                camera.unproject(touchPos); // Agora touchPos tem as coordenadas do mundo


                if (player.isTouchedPlayer(touchPos.x, touchPos.y)) {
                    System.out.println("Jogador tocado!");
                    // Aqui você pode impedir de desenhar linha, por exemplo
                    return true;
                }
                pathPoints.clear();
                bolaEmMovimento = false;
                caminhoIndex = 0;
                addPoint(screenX, screenY);

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                addPoint(screenX, screenY);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                addPoint(screenX, screenY);
                if (pathPoints.size() > 1) {
                    caminhoAnimacao.clear();
                    for (Vector2 ponto : pathPoints) {
                        caminhoAnimacao.add(new Vector2(ponto));
                    }
                    bolaPosicao = new Vector2(caminhoAnimacao.get(0));
                    caminhoIndex = 1;
                    bolaEmMovimento = true;
                }
                pathPoints.clear();
                return true;
            }
        });


    }

    private void addPoint(int screenX, int screenY) {
        Vector2 worldCoords = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        pathPoints.add(new Vector2(worldCoords.x, worldCoords.y));
    }

    private void atualizarBola(float delta) {
        if (!bolaEmMovimento || caminhoIndex >= caminhoAnimacao.size()) return;

        Vector2 destino = caminhoAnimacao.get(caminhoIndex);
        Vector2 direcao = new Vector2(destino).sub(bolaPosicao).nor(); // direção normalizada

        float distancia = velocidade * delta;
        float distAteDestino = bolaPosicao.dst(destino);

        if (distancia >= distAteDestino) {
            bolaPosicao.set(destino);
            caminhoIndex++;
        } else {
            bolaPosicao.mulAdd(direcao, distancia);
        }

        if (caminhoIndex >= caminhoAnimacao.size()) {
            bolaEmMovimento = false; // terminou o caminho
        }
    }
/*
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        atualizarBola(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1); // linha verde
        for (int i = 0; i < pathPoints.size() - 1; i++) {
            shapeRenderer.line(pathPoints.get(i), pathPoints.get(i + 1));
        }
        shapeRenderer.end();

        if (bolaEmMovimento) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 0, 1); // bola amarela
            shapeRenderer.circle(bolaPosicao.x, bolaPosicao.y, 50); // raio = 10
            shapeRenderer.end();
        }
    }*/

    @Override
    public void render(float delta) {

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        MyDraw.drawRelva();
        UtilsGame.cleanScreen();

        float largura = Gdx.graphics.getWidth();
        float altura = Gdx.graphics.getHeight();

        atualizarBola(delta);

        // Dimensões da baliza e margens
        float balizaEspessura = 20f;
        float balizaAltura = altura / 3f;
        float margem = 0f;
        float balizaY = (altura - balizaAltura) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // === CAMPO ===

        shapeRenderer.setColor(Color.WHITE);

        // Contorno do campo (retângulos grossos)
        float espessura = 4f;
        shapeRenderer.rect(0, 0, largura, espessura); // linha inferior
        shapeRenderer.rect(0, altura - espessura, largura, espessura); // linha superior
        shapeRenderer.rect(0, 0, espessura, altura); // esquerda
        shapeRenderer.rect(largura - espessura, 0, espessura, altura); // direita

        // Linha do meio
        float centroX = largura / 2f;
        shapeRenderer.rect(centroX - espessura / 2f, 0, espessura, altura);
        shapeRenderer.end();

        // Círculo central (simples)
        // Para desenhar círculo com linha grossa, ainda precisamos do modo LINE
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(centroX, altura / 2f, 100);
        shapeRenderer.end();

        // === BALIZA ===
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(margem, balizaY, balizaEspessura, balizaAltura); // poste esquerdo
        shapeRenderer.end();

        // === GRANDE ÁREA ===
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);




// Dimensões
        float areaLargura = 100f;
        float areaAltura = balizaAltura + 40f;
        float areaX = margem + balizaEspessura; // depois da baliza
        float areaY = balizaY - 20f; // um pouco acima e abaixo da baliza


// Desenhar linhas da grande área com retângulos finos
// Linha superior
        shapeRenderer.rect(areaX, areaY + areaAltura - espessura, areaLargura, espessura);
// Linha inferior
        shapeRenderer.rect(areaX, areaY, areaLargura, espessura);
// Linha vertical (lado de dentro do campo)
        shapeRenderer.rect(areaX + areaLargura - espessura, areaY, espessura, areaAltura);

        shapeRenderer.end();

        //draw play
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.draw(shapeRenderer);
        shapeRenderer.end();


        // === LINHA DESENHADA COM O DEDO ===
        if (!pathPoints.isEmpty()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.YELLOW);
            for (int i = 0; i < pathPoints.size() - 1; i++) {
                Vector2 p1 = pathPoints.get(i);
                Vector2 p2 = pathPoints.get(i + 1);
                drawThickLine(p1, p2, 3f);
            }
            shapeRenderer.end();
        }

        // === BOLA ===

        if (bolaEmMovimento) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 0, 1); // bola amarela
            shapeRenderer.circle(bolaPosicao.x, bolaPosicao.y, 30); // raio = 10
            shapeRenderer.end();
        }

    }


    private void drawThickLine(Vector2 p1, Vector2 p2, float thickness) {
        Vector2 dir = new Vector2(p2).sub(p1).nor();
        Vector2 normal = new Vector2(-dir.y, dir.x).scl(thickness / 2f);

        Vector2 v1 = new Vector2(p1).add(normal);
        Vector2 v2 = new Vector2(p1).sub(normal);
        Vector2 v3 = new Vector2(p2).sub(normal);
        Vector2 v4 = new Vector2(p2).add(normal);

        shapeRenderer.triangle(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
        shapeRenderer.triangle(v1.x, v1.y, v3.x, v3.y, v4.x, v4.y);
    }


    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        shapeRenderer.dispose();
    }
}
