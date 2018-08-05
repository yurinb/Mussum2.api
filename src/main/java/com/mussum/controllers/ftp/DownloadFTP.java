package com.mussum.controllers.ftp;

import com.mussum.controllers.ftp.utils.FTPcontrol;
import com.mussum.controllers.ftp.utils.Convert;
import com.mussum.controllers.ftp.utils.S;
import com.mussum.models.ftp.Arquivo;
import com.mussum.repository.ArquivoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class DownloadFTP {

    @Autowired
    private ArquivoRepository arqRep;

//    @GetMapping("/api/download")
//    public ResponseEntity getFile(
//            @RequestHeader("professor") String prof,
//            @RequestHeader("dir") String dir,
//            @RequestHeader("fileName") String fileName
//    ) {
    @GetMapping("/api/download/{id}")
    public ResponseEntity getFile(
            @PathVariable Integer id
    ) {
        FTPcontrol ftp = new FTPcontrol();
        try {
            Arquivo arquivo = arqRep.findById(id).get();
            S.out("requesting FILE 1/4: " + arquivo.getDir() + "/" + arquivo.getNome(), this);
            ftp.connect();
            S.out("requesting FILE 2/4: " + arquivo.getDir() + "/" + arquivo.getNome(), this);
            InputStream file = ftp.getFile(arquivo.getDir() + "/", arquivo.getNome());
            S.out("requesting FILE 3/4: " + arquivo.getDir() + "/" + arquivo.getNome(), this);
            if (file == null) {
                ftp.disconnect();
                S.out("ERRO: File not found", this);
                return new ResponseEntity("ERRO: File not found", HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.getNome());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            S.out("requesting FILE 4/4: " + arquivo.getDir() + "/" + arquivo.getNome(), this);
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(file));

//            //ftp.getFtp().completePendingCommand();
//            byte[] bytes = StreamUtils.copyToByteArray(file);
//            //ftp.disconnect();
//            S.out("requesting FILE 5: " + dir + "/" + fileName, this);
//            //file.close();
//            S.out("file served.", this);
//            return new ResponseEntity(bytes, HttpStatus.OK);
        } catch (Exception ex) {
            ftp.disconnect();
            S.out("ERRO: " + ex.getMessage(), this);
            return new ResponseEntity("Erro: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/api/photo")
    public ResponseEntity getProfessorPhoto(@RequestHeader("professor") String prof) {
        FTPcontrol ftp = new FTPcontrol();
        try {
            S.out("requesting user PHOTO: " + prof, this);

            ftp.connect();
            String[] fotosPerfil = ftp.listFiles("\\_res\\perfil_img\\");
            ftp.disconnect();

            String profPhotoName = null;
            for (String foto : fotosPerfil) {
                S.out("FOTO: " + foto, this);
                if (foto.startsWith(prof)) {
                    profPhotoName = foto;
                    break;
                }
            }
            ftp.connect();
            InputStream img = ftp.getFile("\\_res\\perfil_img\\", profPhotoName);

            if (img == null) {
                img.close();
                ftp.disconnect();
                S.out("ERRO: photo " + profPhotoName + " not found", this);
                return new ResponseEntity("ERRO: photo not found", HttpStatus.NOT_FOUND);
            }

            String base64 = Convert.inputStreamToBASE64(img);
            img.close();
            ftp.disconnect();
            S.out("user photo served.", this);
            return new ResponseEntity(base64, HttpStatus.OK);

        } catch (Exception ex) {
            ftp.disconnect();
            S.out("ERRO: " + ex.getMessage(), this);
            return new ResponseEntity("ERRO: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
