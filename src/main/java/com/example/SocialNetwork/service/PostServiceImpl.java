package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private SocialGroupRepository socialGroupRepository;

    @Override
    public List<Post> getAllPostsByUser(Long id) {
        //TODO Uraditi proveru da je ulogovani korisnik ili prijatelj (privatna objava) ili u istoj grupi (ako je objava u grupi)
        List<Post> userPosts = postRepository.findAllById_User(id);

        return getUnexpiredPosts(userPosts);
    }

    @Override
    public List<Post> getAllPostsByLoggedInUser() {
        //TODO Kada bude uradjen login, onda samo treba preko ulogovanog uzeti sve njegove objave
//        List<Post> userPosts = postRepository.findAllById_User(id_user);
        //return getUnexpiredPosts(id_logeedUser);
        return null;
    }

    @Override
    public List<Post> getAllPostsBySocialGroup(Long id) {
        //TODO Uraditi proveru da je ulogovani korisnik u grupi u kojoj se traze objave

        List<Post> postsInGroup = postRepository.findAllById_Social_Group(id);

        return getUnexpiredPosts(postsInGroup);
    }

    @Override
    public Post createPost(Post post) {
        post.setDate(LocalDateTime.now());
        post.setDeleted(false);
        //TODO Dodati ulogovanog korisnika kao korisnika koji je napravio objavu

        return postRepository.save(post);
    }

    @Override
    public Post createPostInGroup(Post post, Long groupId) {
        //TODO Kada bude sredjen email servis, onda treba poslati notifikacije svim clanovima grupe
        //TODO Uzeti ulogovanog usera i proveriti da li je on clan grupe u kojoj vrsi objavu

        post.setDate(LocalDateTime.now());
        post.setDeleted(false);
        //TODO: ulogovani user se stavlja kao user id

        //TODO Baciti gresku ako nema grupe sa ovim ID-om
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).get();

        List<Post> socialGroupPosts = socialGroup.getPosts();
        socialGroupPosts.add(post);
        socialGroupRepository.save(socialGroup);
        post.setSocialGroup(socialGroup);

        return postRepository.save(post);

    }

    @Override
    public Post updatePost(Long id, Post post) {

        //TODO Izbaciti gresku ako objava sa ovim ID-em nije pronadjena u bazi (umesto trenutnog get())
        Post tempPost = postRepository.findById(id).get();

        //TODO confirmPostOwnership provera za ulogovanog korisnika

        if (post.getText() != null && !post.getText().equals("") && post.getText() != tempPost.getText()) {
            tempPost.setText(post.getText());
        }

        return postRepository.save(tempPost);
    }

    @Override
    public void deletePostById(Long id) {

        //TODO Izbaciti gresku ako objava sa ovim ID-em nije pronadjena u bazi (umesto trenutnog get())
        Post post = postRepository.findById(id).get();

        //TODO Proveriti da li je ulogovani admin grupe AKO je ovo objava koja se nalazi u nekoj grupi

        //TODO confirmPostOwnership provera za ulogovanog korisnika
        postRepository.deleteById(id);
    }

    @Override
    public Post makePostPrivate(Long id) {

        //TODO Izbaciti gresku ako objava sa ovim ID-em nije pronadjena u bazi (umesto trenutnog get())
        Post post = postRepository.findById(id).get();

        //TODO confirmPostOwnership provera za ulogovanog korisnika
        post.setType(true);

        return postRepository.save(post);
    }

    @Override
    public Post makePostPublic(Long id) {
        //TODO Izbaciti gresku ako objava sa ovim ID-em nije pronadjena u bazi (umesto trenutnog get())
        Post post = postRepository.findById(id).get();

        //TODO confirmPostOwnership provera
        post.setType(false);

        return postRepository.save(post);
    }

    private List<Post> getUnexpiredPosts(List<Post> unfilteredPosts) {
        List<Post> unexpiredPosts = new ArrayList<>();
        for (Post post : unfilteredPosts) {
            if (!post.getDate().isAfter(post.getDate().plusHours(24))) unexpiredPosts.add(post);
        }
        return unexpiredPosts;
    }

    private boolean confirmPostOwnership(Long userId, Long PostId){
        //TODO Provera da li je ID korisnika koji radi sa objavom jednak ID-u kreatora objave
        return false;
    }
}
