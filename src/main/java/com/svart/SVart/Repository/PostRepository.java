package com.svart.SVart.Repository;

import com.svart.SVart.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
