public class PostListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new PostAdapter();
        recyclerView.setAdapter(adapter);
        
        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(requireContext());
        
        loadPosts();
        
        FloatingActionButton fab = view.findViewById(R.id.fabCreatePost);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CreatePostActivity.class));
        });
        
        return view;
    }

    private void loadPosts() {
        String token = "Bearer " + tokenManager.getToken();
        apiService.getPosts(token).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 