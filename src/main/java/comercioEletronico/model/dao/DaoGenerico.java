package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Identificavel;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

import com.google.gson.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DaoGenerico<T extends Identificavel> {
    protected ArrayList<T> lista = new ArrayList<>();
    protected Gson gson;
    protected String caminhoArquivo;
    protected Type tipoLista;

    public DaoGenerico(String caminhoArquivo, Type tipoLista) {
        this.caminhoArquivo = caminhoArquivo;
        this.tipoLista = tipoLista;
        
        // serve para o Gson formatar e ler as datas da Venda/Promoção
        this.gson = new GsonBuilder()
            // Configuração LocalDateTime
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> 
                new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> 
                LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                
            // Configuração LocalDate
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> 
                new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> 
                LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .create();
    }

    public void inserir(T obj) {
        abrir();
        int idGenerator = 1;
        if (!lista.isEmpty()) {
            idGenerator = lista.getLast().getId() + 1;
        }
        obj.setId(idGenerator);
        lista.add(obj);
        salvar();
    }

    public T listarId(int id) {
        abrir();
        for (T obj : lista) {
            if (obj.getId() == id) {
                return obj;
            }
        }
        return null;
    }

    public void atualizar(T objAtualizado) {
        abrir();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == objAtualizado.getId()) {
                lista.set(i, objAtualizado);
                break;
            }
        }
        salvar();
    }

    public void remover(int id) {
        abrir();
        T obj = listarId(id);
        if (obj != null) {
            lista.remove(obj);
            salvar();
        }
    }

    public ArrayList<T> listar() {
        abrir();
        return lista;
    }

    public void abrir() {
        try (FileReader leitor = new FileReader(caminhoArquivo)) {
            ArrayList<T> dadosLidos = gson.fromJson(leitor, tipoLista);
            if (dadosLidos != null) {
                lista = dadosLidos;
            } else {
                lista = new ArrayList<>();
            }
        } catch (IOException e) {
            lista = new ArrayList<>();
        }
    }

    public void salvar() {
        try (FileWriter escritor = new FileWriter(caminhoArquivo)) {
            String textoJson = gson.toJson(lista);
            escritor.write(textoJson);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}