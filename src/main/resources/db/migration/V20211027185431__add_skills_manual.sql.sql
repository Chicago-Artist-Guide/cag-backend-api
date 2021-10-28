create table add_skills_manual(
    skills_id uuid default uuid_generate_v4(),
    profile_id UUID NOT NULL,
    skills varchar(250),

    primary key (skills_id),
    foreign key (profile_id) references profile(profile_id) on Delete cascade

);