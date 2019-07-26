package com.mn.fileSeeker.core.dao;

import com.mn.fileSeeker.core.model.Condition;
import com.mn.fileSeeker.core.model.FileType;
import com.mn.fileSeeker.core.model.Thing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileIndexDaoImpl implements FileIndexDao {

    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public void insert(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = this.dataSource.getConnection();
            String sql = "insert into thing (name,path,depth,file_type) values (?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4,thing.getFileType().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }

    @Override
    public void delete(Thing thing) {
            Connection connection = null;
            PreparedStatement statement = null;
            try{
                connection = this.dataSource.getConnection();
                String sql = "delete from thing where path=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1,thing.getPath());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                releaseResource(null,statement,connection);
            }
    }

    @Override
    public List<Thing> query(Condition condition) {
        List<Thing> things = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            connection = this.dataSource.getConnection();
            StringBuilder sb = new StringBuilder();
            sb.append("select name,path,depth,file_type from thing ");
            sb.append(" where ");
            sb.append(" name like '").append(condition.getName()).append("%'");
            //search <name> [file_type]
            if(condition.getFileType() != null){
                FileType fileType = FileType.lookupByName(condition.getFileType());
                sb.append(" and file_type ='"+fileType.name()+"'");

            }
            sb.append(" order by depth ").append(condition.getOrderByDepthAsc() ? "asc" : "desc");
            sb.append(" limit ").append(condition.getLimit());
            System.out.println(sb.toString());

            statement = connection.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();

            while(resultSet.next()){
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setFileType(FileType.lookupByName(resultSet.getString("file_type")));

                things.add(thing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(resultSet,statement,connection);
        }
        return things;
    }
    private void releaseResource(ResultSet resultSet, PreparedStatement statement, Connection connection){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
