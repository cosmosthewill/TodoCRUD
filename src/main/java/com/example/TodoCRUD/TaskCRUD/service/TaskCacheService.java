package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.Caching.RedisKeyGen;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TaskCacheService {
    //Constants
    private static final Duration EXP_TIME = Duration.ofMinutes(10);

    //Injection
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;

    public TaskCacheService(RedisTemplate<String, Object> redisTemplate,
                            ObjectMapper objectMapper,
                            TaskRepository taskRepository) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.taskRepository = taskRepository;
    }

    //Get Tasks
    public void setCompleteLoadAllTasksForUser(String username) {
        redisTemplate.opsForValue().set(RedisKeyGen.taskLoadCompletedUser(username), true, EXP_TIME);
    }

    public List<TaskDTO> getCachedTasksForUser(String userName) {
        //Cache miss
        if (!redisTemplate.hasKey(RedisKeyGen.taskLoadCompletedUser(userName))) {
            return null;
        }
        //Cache hit
        List<TaskDTO> returnTaskDTOs = new ArrayList<>();
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Set<Object> taskIds = setOps.members(RedisKeyGen.taskListSetKey(userName));
        if (taskIds == null || taskIds.isEmpty()) return null;
        for (Object taskId : taskIds) {
            String id = taskId.toString();
            HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
            Map<Object, Object> taskMap = hashOps.entries(RedisKeyGen.taskKey(userName, id));
            returnTaskDTOs.add(objectMapper.convertValue(taskMap, TaskDTO.class));
        }
        System.out.println("get tasks in cache");
        return returnTaskDTOs;
    }

    public TaskDTO getCachedTaskById(Integer id, String userName) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        Map<Object, Object> taskMap = hashOps.entries(RedisKeyGen.taskKey(userName, id.toString()));
        if (!taskMap.isEmpty()) {
            System.out.println("get task in cache");
            return objectMapper.convertValue(taskMap, TaskDTO.class);
        } else return null;
    }

    //Update Task
    public void updateCachedTask(TaskDTO taskDTO, String userName) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();

        Map<String, Object> taskMap = objectMapper.convertValue(taskDTO, Map.class);
        hashOps.putAll(RedisKeyGen.taskKey(userName, taskDTO.getId().toString()), taskMap);
        redisTemplate.expire(RedisKeyGen.taskKey(userName, taskDTO.getId().toString()), EXP_TIME);
        setOps.add(RedisKeyGen.taskListSetKey(userName), taskDTO.getId());
        redisTemplate.expire(RedisKeyGen.taskListSetKey(userName), EXP_TIME);
    }

    //Delete Task
    public void deleteCachedTask(String userName, Integer id) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        hashOps.delete(RedisKeyGen.taskKey(userName, id.toString()));
        redisTemplate.opsForSet().remove(RedisKeyGen.taskListSetKey(userName), id);
    }

    //Search with category or description
    public List<TaskDTO> searchTasksByCategoryOrDescription(String userName, String category, String status) {
        List<TaskDTO> userTasks = getCachedTasksForUser(userName);
        List<TaskDTO> filteredTasks = new ArrayList<>();
        if (userTasks == null || userTasks.isEmpty()) {
            //cache all tasks
            userTasks = taskRepository.findByOwnerUserName(userName)
                    .stream()
                    .map(TaskDTO::new)
                    .toList();
            for (TaskDTO task : userTasks) {
                updateCachedTask(task, userName);
            }
            setCompleteLoadAllTasksForUser(userName);
        }
        for (TaskDTO task : userTasks) {
            if ((status == null || task.getStatus().name().toLowerCase().contains(status.toLowerCase())) &&
                    (category == null || task.getOptional_category().toLowerCase().contains(category.toLowerCase()))) {
                filteredTasks.add(task);
            }
        }
        System.out.println("Search in cache");
        return filteredTasks;
    }
}
